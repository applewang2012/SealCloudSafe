/*
 * Copyright (C) 2011 Patrik �kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package safe.cloud.seal.widget;

import android.app.AlertDialog;
import android.content.Context;

/**
 * An interface which defines the contract between a ViewFlow and a
 * FlowIndicator.<br/>
 * A FlowIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.<br/>
 * @author http://blog.csdn.net/finddreams
 */
public class GridViewAlertDialogBuilder extends  AlertDialog.Builder{


	public GridViewAlertDialogBuilder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private AlertDialog alertDialog;

	public GridViewAlertDialogBuilder(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public void showDialog(){
		alertDialog = this.show();
	}

	public void dimissDialog(){
		if (alertDialog != null){
			alertDialog.dismiss();
		}
	}
}
