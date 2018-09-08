# sweet-dialog
![](https://img.shields.io/badge/v1.1.2-pass-blue.svg)
![Release](https://img.shields.io/badge/build-pass-green.svg)
![](https://img.shields.io/badge/dialog-lovely-orange.svg)
## Setup

In order to use this lib, you need to:

Step 1. Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.tientham:sweet-dialog:v1.1.2'
	}

## How to

- Showing sweet-dialog with 2 buttons, animation and title with icon

		final SweetDialog dialog = new SweetDialog(v.getContext(), SweetDialog.CUSTOM_IMAGE_TYPE);
		dialog.setTitleText(getResources().getString(R.string.app_name))
			.setContentText(R.string.dialog_exit_app_title)
			.setCancelText(R.string.dialog_exit_app_btn_cancel)
			.setConfirmText(R.string.dialog_exit_app_btn_yes)
			.showCancelButton(true)
			.setCustomImage(R.drawable.img_girl_1)
			.setCancelClickListener(null)
			.setConfirmClickListener(new SweetDialog.OnSweetClickListener() {
			    @Override
			    public void onClick(SweetDialog sweetDialog) {
				dialog.dismiss();
				MainActivity.this.finish();
			    }
			})
			.show();

- Showing list dialog fragment with icon and text title

Your main activity need to implement SimpleDialog.OnItemClickListener
		
		public class MainActivity extends AppCompatActivity implements View.OnClickListener, SimpleDialog.OnItemClickListener 
		{
			// Your code
		}

To create dialog:

                new SimpleDialogSupportFragment.Builder()
                        .setTitle(R.string.app_language)
                        .setItems(R.array.icon_languages, getIconLanguages())
                        .setRequestCode(REQUEST_CODE_LANGUAGE_ICON_ITEMS)
                        .create().show(getSupportFragmentManager(), "dialog");

		protected int[] getIcons()
		{
			return new int[] { R.drawable.<your_img>,
				R.drawable.<your_img>,
				R.drawable.<your_img>,
				R.drawable.<your_img>,
		};
		}

		protected int[] getIconLanguages()
		{
			return new int[] { R.drawable.lan_uk,
				R.drawable.lan_it,
				R.drawable.lan_vn,
		};
		
		@Override
		public void onItemClick(SimpleDialog dialog, int requestCode, int which)
		{
			if (requestCode == REQUEST_CODE_LANGUAGE_ICON_ITEMS) {
			    switch (which)
			    {
				case 0:
				    //Doing something here;
				    break;
				case 1:	
				    // Doing something here
				    break;
				default:
				    break;

			    }
			}
		}
		

## Roadmap

- Convert this lib to 100% Kotlin

## Who is using us?

- [#App4Autism](https://play.google.com/store/apps/details?id=vn.tientham.visualsupportforautism)
- [#App4Timer](https://play.google.com/store/apps/details?id=tientham.androidtimer)
- [#BluChat](https://play.google.com/store/apps/details?id=tientham.bluetoothdemo)
- [#MovieWiki](https://play.google.com/store/apps/details?id=tientham.movie_wiki)

## Credit

- Badge images are taken from: https://shields.io/#/
- Reference for this project is: https://github.com/pedant/sweet-alert-dialog

## License
	Copyright (c) 2018. Minh Tien TO.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	   http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.




