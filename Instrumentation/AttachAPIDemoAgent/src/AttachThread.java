import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.Base64;
import java.util.List;

/**
 * @author martin
 * @date 2019/9/6
 **/
public class AttachThread extends Thread {

    private final List<VirtualMachineDescriptor> listBefore;

    private final String jar;

    AttachThread(String attachJar, List<VirtualMachineDescriptor> vms) {
        listBefore = vms;  // 记录程序启动时的 VM 集合
        jar = attachJar;
    }

    /**
     * 除了使用VirtualMachine.list()穷举出来
     * 还可以通过pid获取到对应的vm实例，如：
     *
     *  // 通过 VirtualMachine 连接到 运行中的进程 (可以通过 jps 找到进程号)
     *   VirtualMachine vm = null;
     *         try {
     *             // 通过 VirtualMachine 连接到 运行中的进程 (可以通过 jps 找到进程号)
     *             vm = VirtualMachine.attach(<PID>);
     *             vm.loadAgent(<agent.jar 的路径>);
     *         } finally {
     *             if (vm != null) {
     *                 vm.detach();
     *             }
     *         }
     */
    public void run() {
        VirtualMachine vm = null;
        List<VirtualMachineDescriptor> listAfter = null;
        try {
            int count = 0;
            while (true) {
                listAfter = VirtualMachine.list();
                for (VirtualMachineDescriptor vmd : listAfter) {
                    if (!listBefore.contains(vmd)) {
                        // 如果 VM 有增加，我们就认为是被监控的 VM 启动了
                        // 这时，我们开始监控这个 VM
                        vm = VirtualMachine.attach(vmd);
                        break;
                    }
                }
                Thread.sleep(500);
                count++;
                if (null != vm || count >= 10) {
                    break;
                }
            }
            vm.loadAgent(jar);
            vm.detach();
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("开始-------");
        new AttachThread("agentAttachAPIDemo.jar", VirtualMachine.list()).start();
    }

}

