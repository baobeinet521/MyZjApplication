import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MineDebugActivity  extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "hello CC", Toast.LENGTH_SHORT).show();
        //需要单独安装运行，但不需要入口页面（只需要从主app中调用此组件）时，
        // 可直接finish当前activity
        finish();
    }
}
