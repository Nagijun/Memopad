package sample.application.memopad;





import android.app.Activity;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;//�ǉ�p69
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.MenuInflater;


public class MemopadActivity extends Activity {
	boolean memoChanged = false;//�ǉ�p69
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		MenuInflater mi = this.getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		
		return super.onCreateOptionsMenu(menu);
		
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
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
    protected void onCreate(Bundle savedInstanceState) {//�܂���`����Ă�����
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
     
        EditText et = (EditText) this.findViewById(R.id.editText1);
		SharedPreferences pref = this.getSharedPreferences("MemoPrefs",MODE_PRIVATE);
		memoChanged = pref.getBoolean("memoChanged", false);//�ǉ�p71
		et.setText(pref.getString("memo",""));
		et.setSelection(pref.getInt("cursor",0));
		
		TextWatcher tw = new TextWatcher() {//�ǉ�p69�`p70

			public void afterTextChanged(Editable arg0) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				memoChanged = true;
			}
			
		};
		
		et.addTextChangedListener(tw);//�ǉ�p70
  }
    
    @Override
    public void onStop() {
    	super.onStop();
    	EditText et = (EditText) this.findViewById(R.id.editText1);
    	SharedPreferences pref = this.getSharedPreferences("Memoprefs",MODE_PRIVATE);
    	SharedPreferences.Editor editor = pref.edit();
    	editor.putString("memo",et.getText().toString());
    	editor.putInt("cursor", Selection.getSelectionStart(et.getText()));
    	editor.putBoolean("memoChanged", memoChanged);//�ǉ�p71
    	editor.commit();
    	
    }
    
    public void saveMemo() {
		EditText et =  (EditText) this.findViewById(R.id.editText1);
		String title;
		String memo = et.getText().toString();//�ϐ�memo�Ƀ����̓��e��ۑ�
		
		if(memo.trim().length() > 0) {//�����̕��������擾���A�P�����ȏ�̂Ƃ����s
			if(memo.indexOf("\n") == -1) {//�����̒��ɉ��s���Ȃ������Ƃ��Ɏ��s
				title = memo.substring(0,Math.min(memo.length(),20));//
			} else {//�����̒��ɉ��s���ŏ��ɏo������ʒu��memo.indexOf()�Ɋi�[
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