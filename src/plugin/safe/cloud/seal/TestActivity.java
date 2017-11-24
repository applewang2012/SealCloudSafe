package plugin.safe.cloud.seal;

import com.ryg.dynamicload.DLBasePluginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import plugin.safe.cloud.seal.zxingview.ScanCodeActivity;


public class TestActivity extends DLBasePluginActivity {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            if (data != null){
                try {
                    String scanResult = data.getExtras().getString("result");
//                    TextView textview = (TextView)findViewById(R.id.id_show_text);
//                    textview.setText(scanResult);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        Button click = (Button)findViewById(R.id.id_click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(that, ScanCodeActivity.class), 1);
//                Intent intent = new Intent(that, IdentifyDetectActivity.class);
//                intent.putExtra("real_name", "王明国");
//                intent.putExtra("real_idcard", "370881198411094833");
//                startActivityForResult(intent ,1);

//                Intent intent = new Intent(that, CloudwalkActivity.class);
//                intent.putExtra("real_name", "王明国");
//                intent.putExtra("real_idcard", "370881198411094833");
//                startActivityForResult(intent ,1);
            }
        });



    }
}
