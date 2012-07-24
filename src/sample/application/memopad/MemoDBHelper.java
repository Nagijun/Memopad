package sample.application.memopad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class MemoDBHelper extends SQLiteOpenHelper {
	/* MemoDBHelper�̓R���X�g���N�^ */
	 
	public static String name = "memos.db";
	public static CursorFactory factory = null;
	public static Integer version = 1;
	
	public String name2 = "memos.db";//�C���X�^���X�ϐ�
	
	public MemoDBHelper(Context context, String name, CursorFactory factory,
			Integer version) {
		super(context, name, factory, version);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}
	
	public MemoDBHelper(Context context) {
		super(context,name,factory,version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		String sql = "CREATE TABLE memoDB ("
		+android.provider.BaseColumns._ID
		+" INTEGER PRIMARY KEY AUTOINCREMENT, title Text, memo TEXT);";
		db.execSQL(sql);//

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}
}


