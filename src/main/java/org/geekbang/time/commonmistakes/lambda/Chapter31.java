package org.geekbang.time.commonmistakes.lambda;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

/**
 * @author kongwiki@163.com
 * @since 2021/1/30
 */
public class Chapter31 {

    public void lambda() {
        CountDownLatch lan = new CountDownLatch(2);
        // 匿名类
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("this is 匿名类结果");
            }
        }).start();
        lan.countDown();
        // lambda表达式
        new Thread(() -> System.out.println("this is lambda start thread")).start();
        lan.countDown();
        try {
            lan.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("this is main");
    }

    public static void supplier() {
        Supplier<String> stringSupplier = () -> "string";
        Supplier<String> anotherStringSupplier = String::new;

    }

    // stream 简化操作的示例
    // 把整数列表转换为 Point2D 列表；
    // 遍历 Point2D 列表过滤出 Y 轴 >1 的对象；
    // 计算 Point2D 点到原点的距离；累加所有计算出的距离，并计算距离的平均值
    public static double useStream(List<Integer> ints) {
        List<Point2D> point2DList = new ArrayList<>();
        for (Integer i : ints) {
            point2DList.add(new Point2D.Double((double) i % 3, (double) i / 3));
        }
        double total = 0;
        int count = 0;
        for (Point2D point2D : point2DList) {
            if (point2D.getY() > 1) {
                double distance = point2D.distance(0, 0);
                total += distance;
                count++;
            }
        }
        return count > 0 ? total / count : 0;
    }

    public static double useStreamII(List<Integer> ints) {
        return ints.stream().map(i -> new Point2D.Double((double)i%3, (double)i/3))
            .filter(point->point.getY() > 1)
            .mapToDouble(point->point.distance(0, 0))
            .average()
            .orElse(0);
    }

    public static void main(String[] args) throws InterruptedException {
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        System.out.println(useStream(ints));
        System.out.println(useStreamII(ints));
    }
}
