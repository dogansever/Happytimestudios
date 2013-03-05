/***
  Copyright (c) 2008-2012 CommonsWare, LLC
  Licensed under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
  by applicable law or agreed to in writing, software distributed under the
  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
  OF ANY KIND, either express or implied. See the License for the specific
  language governing permissions and limitations under the License.
  
  From _The Busy Coder's Guide to Advanced Android Development_
    http://commonsware.com/AdvAndroid
 */

package com.example.ttwidgetdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class AppsTriggerWidgetActivity extends Activity {
	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);

		String index = getIntent().getStringExtra(AppsWidgetService.EXTRA_ITEM);
		// to do open app
		String uri = AppsWidgetService.appList.get(Integer.parseInt(index));
		try {
			Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(uri);
			startActivity(LaunchIntent);
			Toast.makeText(this, "Açılıyor...", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(this, "Uygulama bulunamadı. Markete yönlendiriliyorsunuz...", Toast.LENGTH_SHORT).show();
			String inURL = "market://details?id=" + uri;
			Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
			browse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(browse);
			e.printStackTrace();
		}

		// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		// startActivity(intent);

		finish();
	}
}