package safe.cloud.seal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import safe.cloud.seal.model.SealInfoModel;
import safe.cloud.seal.model.UniversalAdapter;
import safe.cloud.seal.model.UniversalViewHolder;
import safe.cloud.seal.widget.GridViewAlertDialogBuilder;

public class MainActivity extends Activity{

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_main); 
		
		
		
		showAlertDialog(null, null, null);
	}
	
	
	private void showAlertDialog(final TextView text, final String tag, final String[] items) {  
		 List<SealInfoModel> dialogItemList = new ArrayList<>();
		 final GridViewAlertDialogBuilder builder = new GridViewAlertDialogBuilder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT);
		 
		 for (int i = 0; i < 10; i++){
			 SealInfoModel seal = new SealInfoModel();
			 seal.setSelectedName("选项 - "+i);
			 dialogItemList.add(seal);
		 }
		 
		  View view = LayoutInflater.from(this).inflate(R.layout.dialog_style_layout, null);
		  GridView gridView = (GridView) view.findViewById(R.id.id_dialog_gridview);	
		  gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		  builder.setView(view);
		  
		  UniversalAdapter adapter = new UniversalAdapter<SealInfoModel>(MainActivity.this, R.layout.dialog_style_item_layout, dialogItemList) {

				@Override
				public void convert(UniversalViewHolder holder, SealInfoModel info) {
					View holderView = holder.getConvertView();
					TextView addressTextView = (TextView)holderView.findViewById(R.id.item_gridview_text);
					addressTextView.setText(info.getSelectedName());
				}
			};
			
			gridView.setAdapter(adapter);
			
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "item click  position  "+arg2, Toast.LENGTH_SHORT).show();
					
					builder.dimissDialog();
				}
			});

		builder.showDialog();
	}
	
	
	
}
