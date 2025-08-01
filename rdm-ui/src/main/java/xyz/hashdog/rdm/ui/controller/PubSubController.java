package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import xyz.hashdog.rdm.redis.client.RedisMonitor;
import xyz.hashdog.rdm.redis.client.RedisPubSub;
import xyz.hashdog.rdm.ui.sampler.event.DefaultEventBus;
import xyz.hashdog.rdm.ui.sampler.event.ThemeEvent;
import xyz.hashdog.rdm.ui.sampler.theme.SamplerTheme;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static xyz.hashdog.rdm.ui.util.LanguageManager.language;

public class PubSubController extends BaseKeyController<ServerTabController> implements Initializable {


    public WebView webView;

    public StackPane webViewContainer;
    private final StringBuilder tableContent = new StringBuilder();
    public TextField subChannel;
    private int messageCounter = 0;
    private static final int MAX_MESSAGES = 1000;
    private Thread subscribeThread;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webView.setContextMenuEnabled(false);
        initCustomContextMenu();
        initWebView();
        applyTheme();
        DefaultEventBus.getInstance().subscribe(ThemeEvent.class, e -> {
            applyTheme();
        });
        addSubscriptionMessage("19:48:06 31 Jul 2025", "mychannel", "22");
        addSubscriptionMessage("19:48:09 31 Jul 2025", "mychannel", "123");



    }

    /**
     * 初始化自定义上下文菜单
     */
    private void initCustomContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // 清空日志
        MenuItem clearItem = new MenuItem(language("server.monitor.clear"));
        clearItem.setOnAction(e -> clearMessages());

        // 复制选中文本
        MenuItem copyItem = new MenuItem(language("main.edit.copy"));
        copyItem.setOnAction(e -> copySelectedText());

        // 全选
        MenuItem selectAllItem = new MenuItem(language("main.edit.selectall"));
        selectAllItem.setOnAction(e -> selectAllText());

        // 保存日志
        MenuItem saveItem = new MenuItem(language("server.monitor.save"));
        saveItem.setOnAction(e -> saveLogs());

        // 添加菜单项
        contextMenu.getItems().addAll(
                clearItem,
                new SeparatorMenuItem(),
                copyItem,
                selectAllItem,
                new SeparatorMenuItem(),
                saveItem
        );

        // 设置右键菜单事件
        webView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                // 检查是否有选中文本，来决定是否启用复制选项
                String selectedText = (String) webView.getEngine().executeScript("window.getSelection().toString();");
                copyItem.setDisable(selectedText == null || selectedText.isEmpty());

                contextMenu.show(webView, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });

        // 点击其他地方隐藏菜单
        webView.setOnMousePressed(event -> {
            if (contextMenu.isShowing() && event.getButton() != MouseButton.SECONDARY) {
                contextMenu.hide();
            }
        });
    }



    /**
     * 复制选中文本
     */
    private void copySelectedText() {
        String selectedText = (String) webView.getEngine().executeScript("window.getSelection().toString();");
        if (selectedText != null && !selectedText.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedText);
            clipboard.setContent(content);
        }
    }

    /**
     * 全选文本
     */
    private void selectAllText() {
        webView.getEngine().executeScript(
                "var selection = window.getSelection();" +
                        "var range = document.createRange();" +
                        "range.selectNodeContents(document.getElementById('log-container'));" +
                        "selection.removeAllRanges();" +
                        "selection.addRange(range);"
        );
    }

    /**
     * 保存日志
     */
    private void saveLogs() {
        // 实现保存日志功能
        System.out.println("保存日志功能待实现");
        // 可以使用FileChooser来实现文件保存功能
    }







    /**
     * 应用暗色主题
     */
    public void applyTheme()  {
        SamplerTheme theme = ThemeManager.getInstance().getTheme();
        int fontSize = ThemeManager.getInstance().getFontSize();
        String fontFamily = ThemeManager.getInstance().getFontFamily();
        Map<String, String> colors = null;
        try {
            colors = theme.parseColors();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String c1 = colors.get("-color-bg-subtle");
        String c2 = colors.get("-color-fg-default");
        String c3 = colors.get("-color-success-fg");
        String c4 = colors.get("-color-accent-fg");
        String c5 = colors.get("-color-border-default");
        updateAllStyles(
                c5,
                fontFamily,
                c1,    // body背景色
                c2,           // body文字色
                c3,           // 时间戳颜色
                c2,           // 客户端信息颜色
                c4,           // 类型颜色
                c2,           // 命令颜色
                fontSize+"px"               // 字体大小
        );
    }


    private void initWebView() {
        // 初始化HTML内容，包含表格结构
        String htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { 
                    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
                    background-color: rgb(44,44,46); 
                    color: #fff; 
                    margin: 0; 
                    padding: 10px;
                    font-size: 14px;
                }
                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 10px;
                    table-layout: fixed; /* 固定表格布局确保对齐 */
                }
                thead {
                    position: sticky;
                    top: 0;
                    background-color: rgb(60,60,62);
                }
                th {
                    background-color: rgb(60,60,62);
                    color: #fff;
                    text-align: left;
                    padding: 8px 12px;
                    border-bottom: 2px solid #555;
                    font-weight: 600;
                    white-space: nowrap;
                }
                td {
                    padding: 6px 12px;
                    border-bottom: 1px solid #444;
                    text-align: left;
                    vertical-align: top;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }
                tr:hover {
                    background-color: rgba(255,255,255,0.05);
                }
                .timestamp { 
                    color: #4EC9B0; 
                    width: 25%;
                }
                .channel { 
                    color: #C586C0; 
                    width: 25%;
                }
                .message { 
                    color: #9CDCFE; 
                    width: 50%;
                    word-break: break-all;
                }
                #message-table {
                    width: 100%;
                }
            </style>
        </head>
        <body>
            <table id="message-table">
                <thead>
                    <tr>
                        <th class="timestamp">时间</th>
                        <th class="channel">频道</th>
                        <th class="message">消息</th>
                    </tr>
                </thead>
                <tbody id="table-body">
                </tbody>
            </table>
        </body>
        </html>
        """;

        webView.getEngine().loadContent(htmlContent);
    }


    /**
     * 动态更新整个样式表
     * @param bodyBgColor body背景颜色
     * @param bodyColor body文字颜色
     * @param timestampColor 时间戳颜色
     * @param hostColor 客户端信息颜色
     * @param commandColor 命令颜色
     * @param fontSize 字体大小
     */
    public void updateAllStyles(String borderColor,String fontFamily,String bodyBgColor, String bodyColor, String timestampColor,
                                String hostColor, String typeColor,String commandColor, String fontSize) {
        Platform.runLater(() -> {
            String cssContent = String.format("""
                body { 
                    font-family: %s; 
                    background-color: %s; 
                    color: %s; 
                    margin: 0; 
                    padding: 5px;
                    font-size: %s;
                }
                .log-line { margin: 2px 0; }
                .timestamp { color: %s; }
                .host { color: %s; }
                .type { color: %s; }
                .command { color: %s; }
                """, fontFamily,bodyBgColor, bodyColor, fontSize, timestampColor, hostColor, typeColor,commandColor);

            updateStyleSheet(cssContent);
        });
        webViewContainer.setStyle(String.format("-fx-border-color: %s; -fx-border-width: 1px; -fx-border-style: solid;",borderColor));
    }
    private static final List<String> CLASS=List.of("timestamp", "host","type" ,"command");


    /**
     * 更新样式表
     * @param cssContent 新的CSS内容
     */
    private void updateStyleSheet(String cssContent) {
        String script = String.format("""
            (function() {
                var newStyle = document.createElement('style');
                newStyle.type = 'text/css';
                newStyle.innerHTML = `%s`;
                var head = document.getElementsByTagName('head')[0];
                var oldStyle = document.getElementById('dynamic-style');
                if (oldStyle) {
                    head.removeChild(oldStyle);
                }
                newStyle.id = 'dynamic-style';
                head.appendChild(newStyle);
            })();
            """, cssContent.replace("`", "\\`"));

        webView.getEngine().executeScript(script);
    }
    /**
     * 添加订阅消息到表格
     * @param timestamp 时间戳
     * @param channel 频道
     * @param message 消息内容
     */
    public void addSubscriptionMessage(String timestamp, String channel, String message) {
        Platform.runLater(() -> {
            // 创建表格行，确保数据居左对齐
            String tableRow = String.format(
                    "<tr>" +
                            "<td class='timestamp'>%s</td>" +
                            "<td class='channel'>%s</td>" +
                            "<td class='message'>%s</td>" +
                            "</tr>",
                    escapeHtml(timestamp),
                    escapeHtml(channel),
                    escapeHtml(message)
            );

            tableContent.append(tableRow);
            messageCounter++;

            // 限制最大消息数
            if (messageCounter > MAX_MESSAGES) {
                // 简单处理：清空并重新添加（实际应用中可能需要更精确的处理）
                String currentContent = tableContent.toString();
                int firstRowEnd = currentContent.indexOf("</tr>") + 5;
                if (firstRowEnd > 0 && firstRowEnd < currentContent.length()) {
                    tableContent.delete(0, firstRowEnd);
                }
                messageCounter--;
            }

            // 更新表格内容
            String script = String.format(
                    "var tbody = document.getElementById('table-body');" +
                            "tbody.innerHTML = `%s`;" +
                            "window.scrollTo(0, document.body.scrollHeight);",
                    tableContent.toString().replace("`", "\\`")
            );

            webView.getEngine().executeScript(script);
        });
    }

    /**
     * HTML转义，防止XSS攻击并保持格式
     * @param text 原始文本
     * @return 转义后的文本
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("\n", "<br>")
                .replace(" ", "&nbsp;");
    }

    /**
     * 清空消息表格
     */
    private void clearMessages() {
        Platform.runLater(() -> {
            tableContent.setLength(0);
            messageCounter = 0;
            webView.getEngine().executeScript("document.getElementById('table-body').innerHTML = '';");
        });
    }

    @Override
    public void close() {
        if (subscribeThread != null && subscribeThread.isAlive()) {
            // 中断监控线程
            subscribeThread.interrupt();
            subscribeThread = null;
        }
    }

    public void subscribe(ActionEvent actionEvent) {
        subscribeThread = new Thread(() -> {
            this.redisClient.subscribe(new RedisPubSub() {
                @Override
                public void onMessage(String channel, String msg) {
                    addSubscriptionMessage(LocalDateTime.now().toString(),channel,msg);
                }
            },subChannel.getText());
        });
        subscribeThread.setDaemon(true);
        subscribeThread.start();
    }
}























