package sample.application.memopad;





import android.app.Activity;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;//追加p69
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.MenuInflater;


public class MemopadActivity extends Activity {
	boolean memoChanged = false;//追加p69
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
    	if(resultCode == RESULT_OK) {
    		EditText et = (EditText) this.findViewById(R.id.editText1);
    		
    		switch(requestCode) {
    		case 0:
    			et.setText(data.getStringExtra("text"));
    			memoChanged = false;
    			break;
    		}
    	}
    	
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO 自動生成されたメソッド・スタブ
		MenuInflater mi = this.getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		
		return super.onCreateOptionsMenu(menu);
		
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		EditText et = (EditText) this.findViewById(R.id.editText1);
		switch(item.getItemId()) {
		case R.id.menu_save:
			this.saveMemo();
			break;
		case R.id.menu_open:
			if(memoChanged) saveMemo();
			Intent i = new Intent(this,MemoList.class);
			this.startActivityForResult(i,0);
			break;
		case R.id.menu_new:
			if(memoChanged) saveMemo();
			et.setText("");
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	/** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {//まだ定義されている状態
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
     
        EditText et = (EditText) this.findViewById(R.id.editText1);
		SharedPreferences pref = this.getSharedPreferences("MemoPrefs",MODE_PRIVATE);
		memoChanged = pref.getBoolean("memoChanged", false);//追加p71
		et.setText(pref.getString("memo",""));
		et.setSelection(pref.getInt("cursor",0));
		
		TextWatcher tw = new TextWatcher() {//追加p69～p70

			public void afterTextChanged(Editable arg0) {
				// TODO 自動生成されたメソッド・スタブ
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自動生成されたメソッド・スタブ
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自動生成されたメソッド・スタブ
				memoChanged = true;
			}
			
		};
		
		et.addTextChangedListener(tw);//追加p70
  }
    
    @Override
    public void onStop() {
    	super.onStop();
    	EditText et = (EditText) this.findViewById(R.id.editText1);
    	SharedPreferences pref = this.getSharedPreferences("Memoprefs",MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();
    	editor.putString("memo",et.getText().toString());
    	editor.putInt("cursor", Selection.getSelectionStart(et.getText()));
    	editor.putBoolean("memoChanged", memoChanged);//追加p71
    	editor.commit();
    	
    }
    
    public void saveMemo() {
		EditText et =  (EditText) this.findViewById(R.id.editText1);
		String title;
		String memo = et.getText().toString();//変数memoにメモの内容を保存
		
		if(memo.trim().length() > 0) {//メモの文字数を取得し、１文字以上のとき実行
			if(memo.indexOf("\n") == -1) {//メモの中に改行がなかったときに実行
				title = memo.substring(0,Math.min(memo.length(),20));//
			} else {//メモの中に改行が最初に出現する位置をmemo.indexOf()に格納
				title = memo.substring(0,Math.min(memo.indexOf("\n"),20));
			}
			String ts = Jp_String.jpString();
			MemoDBHelper memos = new MemoDBHelper(this);
			SQLiteDatabase db = memos.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("title", title+"\n"+ts);
			values.put("memo", memo);
			db.insertOrThrow("memoDB", null, values);
			memos.close();
			memoChanged = false;
			}
		}
}