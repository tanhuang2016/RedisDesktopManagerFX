package xyz.hashdog.rdm.ui.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 最近记录保存，用于搜索记录，打开最近连接等功能
 * @author th
 * @version 2.0.0
 * @since 2025/7/20 13:10
 */
public class RecentHistory<T> {

    private int size = 5;
    private final LinkedHashSet<T> historySet = new LinkedHashSet<>();
    private Noticer<T> noticer;

    public RecentHistory(int size, List<T> data,Noticer<T> noticer) {
        this.size = size;
        this.noticer = noticer;
        historySet.addAll(data);
    }

    public void add(T query) {
        historySet.remove(query);
        if (historySet.size() >= size) {
            historySet.removeLast();
        }
        historySet.addFirst(query);
        notice();
    }

    private void notice() {
        noticer.notice(get());
    }

    public void clear(){
        historySet.clear();
        notice();
    }

    public List<T> get() {
        List<T> list = new ArrayList<>(historySet);
        if (list.size() > size) {
            return list.subList(0, size);
        }
        return list;
    }

    public int size() {
        return size;
    }

    public static interface Noticer<T>{
        void notice(List<T> list);

    }
}
