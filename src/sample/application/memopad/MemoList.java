package sample.application.memopad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;
import android.content.DialogInterface;
import android.content.Intent;


public class MemoList extends ListActivity {
	
	public static final String[] cols = {"title","memo",android.provider.BaseColumns._ID,};
	public MemoDBHelper memos;


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO 自動生成されたメソッド・スタブ
		super.onListItemClick(l, v, position, id);
		this.memos = new MemoDBHelper(this);
		SQLiteDatabase db = memos.getWritableDatabase();
		Cursor cursor = db.query("memoDB", MemoList.cols, "_ID="+String.valueOf(id), null, null, null,null);
		startManagingCursor(cursor);
		int idx = cursor.getColumnIndex("memo");
		cursor.moveToFirst();
		Intent i = new Intent();
		
		i.putExtra("text",cursor.getString(idx));
		this.setResult(Activity.RESULT_OK,i);
		this.memos.close();
		this.finish();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.memolist);
		this.showMemos(getMemos());
		
		ListView lv = (ListView) this.findViewById(android.R.id.list);
		this.registerForContextMenu(lv);
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Cursor cursor = getMemos();
		startManagingCursor(cursor);
		cursor.moveToPosition(info.position);
		final int columnid = cursor.getInt(2);
		
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(R.string.memodb_delete);
		ab.setMessage(R.string.memodb_confirm_delete);
		ab.setPositiveButton(R.string.button_ok,new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				SQLiteDatabase db = memos.getWritableDatabase();
				db.delete("memoDB", "_id="+columnid, null);
				db.close();
				showMemos(getMemos());
			}
		});
		ab.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		ab.show();
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO 自動生成されたメソッド・スタブ
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.contextmenu, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public void showMemos(Cursor cursor) {
		// TODO 自動生成されたメソッド・スタブ
		if(cursor != null) {
			String[] from = {"title"};
			int[] to = {android.R.id.text1};
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this,android.R.layout.simple_list_item_1,
					cursor,from,to);
			this.setListAdapter(adapter);
		}
		this.memos.close();
	}

	public Cursor getMemos() {
		// TODO 自動生成されたメソッド・スタブ
			this.memos = new MemoDBHelper(this);
			SQLiteDatabase db = memos.getReadableDatabase();
			Cursor cursor = db.query("memoDB", MemoList.cols, null, null, null, null, null);
			this.startManagingCursor(cursor);
			return cursor;	
	}

}
