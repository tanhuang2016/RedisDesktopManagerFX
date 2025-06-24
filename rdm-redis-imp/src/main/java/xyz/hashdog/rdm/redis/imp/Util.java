package xyz.hashdog.rdm.redis.imp;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.jcajce.JcePKCSPBEInputDecryptorProviderBuilder;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;



public class Util {


    public static javax.net.ssl.SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile, final String passwordStr)  {
        char[] password = passwordStr==null?null:passwordStr.toCharArray();
        return getSocketFactory(caCrtFile, crtFile, keyFile, password);
    }
    /**
     * 创建 SSLSocketFactory 工厂
     *
     * @param caCrtFile 服务端 CA 证书
     * @param crtFile 客户端 CRT 文件
     * @param keyFile 客户端 Key 文件
     * @param password SSL 密码，随机
     * @return {@link javax.net.ssl.SSLSocketFactory}
     * @throws Exception 异常
     */
    public static javax.net.ssl.SSLSocketFactory getSocketFactory(final String caCrtFile, final String crtFile, final String keyFile, final char[] password)  {
        InputStream caInputStream = null;
        InputStream crtInputStream = null;
        InputStream keyInputStream = null;
        try {
            Security.addProvider(new BouncyCastleProvider());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // load CA certificate
            caInputStream = Files.newInputStream(Paths.get(caCrtFile));
            X509Certificate caCert = null;
            while (caInputStream.available() > 0) {
                caCert = (X509Certificate) cf.generateCertificate(caInputStream);
            }
            // load client certificate
            crtInputStream = Files.newInputStream(Paths.get(crtFile));
            X509Certificate cert = null;
            while (crtInputStream.available() > 0) {
                cert = (X509Certificate) cf.generateCertificate(crtInputStream);
            }

            // load client private key
            keyInputStream = Files.newInputStream(Paths.get(keyFile));
            PEMParser pemParser = new PEMParser(new InputStreamReader(keyInputStream));
            Object object = pemParser.readObject();
            PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder().build(password);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair key;
            if (object instanceof PEMEncryptedKeyPair) {
                key = converter.getKeyPair(((PEMEncryptedKeyPair) object).decryptKeyPair(decProv));
            } else if(object instanceof PKCS8EncryptedPrivateKeyInfo){
                // 解密私钥
                PKCS8EncryptedPrivateKeyInfo encryptedInfo = (PKCS8EncryptedPrivateKeyInfo) object;
                PrivateKey privateKey = new JcaPEMKeyConverter().getPrivateKey(
                        encryptedInfo.decryptPrivateKeyInfo(
                                new JcePKCSPBEInputDecryptorProviderBuilder()
                                        .setProvider("BC")
                                        .build(password)
                        )
                );

                PublicKey publicKey = cert.getPublicKey();
                key=new KeyPair(publicKey, privateKey);
            } else{
                key = converter.getKeyPair((PEMKeyPair) object);
            }
            pemParser.close();

            // CA certificate is used to authenticate server
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            caKs.setCertificateEntry("ca-certificate", caCert);
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(caKs);

            // client key and certificates are sent to server so it can authenticate
            // us
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry("certificate", cert);
            ks.setKeyEntry("private-key", key.getPrivate(), password, new java.security.cert.Certificate[]{cert});
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // finally, create SSL socket factory
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            return context.getSocketFactory();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            close(caInputStream,crtInputStream,keyInputStream);
        }
    }

    private static void close(Closeable... closeable) {
        for (Closeable close : closeable) {
            if (null != close) {
                try {
                    close.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Session createTunnel(String sshUserName, String sshHost, int sshPort,String sshPassword,String sshPrivateKey, String sshPassphrase)  {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(sshUserName,sshHost,sshPort);
            session.setPassword(sshPassword);
            if(sshPrivateKey != null){
                jsch.addIdentity(sshPrivateKey,sshPassphrase);
            }
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return session;
            
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static int portForwardingL( Session session,String rhost,  int rport){
        try {
            return session.setPortForwardingL(0, rhost, rport);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        try {
            String dir = "E:\\ha\\BF\\compose\\bitnami_redis_ssl\\certs有密码\\";
            javax.net.ssl.SSLSocketFactory socketFactory = getSocketFactory(dir+"ca.crt", dir+"redis.crt", dir+"redis.key", "redis123".toCharArray());
            dir = "E:\\ha\\BF\\compose\\bitnami_redis_ssl\\certs无密码\\";
            socketFactory = getSocketFactory(dir+"ca.crt", dir+"redis.crt", dir+"redis.key", "redis123".toCharArray());
            dir = "E:\\ha\\BF\\compose\\bitnami_redis_ssl\\certs\\";
            socketFactory = getSocketFactory(dir+"ca.crt", dir+"redis.crt", dir+"redis.key", "redis123".toCharArray());

            System.out.println(666);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
