import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 9:52 2020/7/24
 * @Modified By:
 */
public class MemeryInfoTest {

    public static void main(String[] args) throws InterruptedException {

        for (;;) {
            monitor();
            Thread.sleep(5000);
        }

    }

    private static void monitor() {
        String startTime = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
        System.out.println("start time: " + startTime);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long init = heapMemoryUsage.getInit();
        long used = heapMemoryUsage.getUsed();
        long max = heapMemoryUsage.getMax();
        System.out.println(String.format("heapMemory: init:[%sM], max:[%sM], used:[%sM]",
                byteToMByte(init),
                byteToMByte(max),
                byteToMByte(used)));
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        init = nonHeapMemoryUsage.getInit();
        used = nonHeapMemoryUsage.getUsed();
        max = nonHeapMemoryUsage.getMax();

        System.out.println(String.format("nonHeapMemory: init:[%sM], max:[%sM], used:[%sM]",
                byteToMByte(init),
                byteToMByte(max),
                byteToMByte(used)));


        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalMSize = operatingSystemMXBean.getTotalPhysicalMemorySize();
        long freeMSize = operatingSystemMXBean.getFreePhysicalMemorySize();
        double cpuLoad = operatingSystemMXBean.getSystemCpuLoad();
        long committedVirtualMemorySize = operatingSystemMXBean.getCommittedVirtualMemorySize();
        String osName = System.getProperty("os.name");

        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println(String.format("本地IP: %s, 主机名: %s", localHost.getHostAddress(), localHost.getHostName()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("os info: {name:%s, totalMemory:%sM, freeMemory:%sM, cpuLoad:%2.2f/100, virtualMemory:%sM}",
                osName,
                byteToMByte(totalMSize),
                byteToMByte(freeMSize),
                cpuLoad * 100,
                byteToMByte(committedVirtualMemorySize)));
    }

    private static int byteToMByte(long num) {
        return (int) (num >> 20);
    }


    private static void connectRemoteServer() {
        // new Connection("zdbd02", 22);
    }
}
