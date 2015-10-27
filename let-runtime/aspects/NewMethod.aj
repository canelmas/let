import android.widget.Toast;
import android.content.Context;

aspect NewMethodAspect {

    interface LovelyInterface {}

    declare parents : com.canelmas.permissioncompat..* implements LovelyInterface;

    public String LovelyInterface.toString() {
        return "ITS WORKING!!!";
    }

    public void LovelyInterface.onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("yoksa yoksa");
    }

}
