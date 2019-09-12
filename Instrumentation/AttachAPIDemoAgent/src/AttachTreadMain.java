import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * @author martin
 * @date 2019/9/11
 **/
public class AttachTreadMain {


    public static void main(String[] args) {

        String pid = args[0];
        VirtualMachine vm = null;

        System.out.println("pid=" + pid);
        try {
             vm = VirtualMachine.attach(pid);
             vm.loadAgent("E:\\Work\\MultiProject\\JAVASE6Agent\\MyAgent2\\target\\MyAgent2.jar");
        } catch (AttachNotSupportedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AgentLoadException e) {
            e.printStackTrace();
        } catch (AgentInitializationException e) {
            e.printStackTrace();
        } finally {
            if(vm != null) {
                try {
                    vm.detach();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
