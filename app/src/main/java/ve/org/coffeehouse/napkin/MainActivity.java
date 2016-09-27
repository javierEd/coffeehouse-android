package ve.org.coffeehouse.napkin;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ve.org.coffeehouse.napkin.data.Note;
import ve.org.coffeehouse.napkin.data.NoteListBaseAdapter;
import ve.org.coffeehouse.napkin.data.NotesDbHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NotesDbHelper db;

    ListView listView;
    ArrayList<Note> values;
    NoteListBaseAdapter adapter;

    Integer selected_note_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        EditText editText = (EditText) findViewById(R.id.note_input);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    saveNote(v);
                    handled = true;
                }
                return handled;
            }
        });

        db = new NotesDbHelper(this);

        listView = (ListView) findViewById(R.id.notes_list);

        values = db.getAllNotes();

        adapter = new NoteListBaseAdapter(this, values);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView hidden_id = (TextView) view.findViewById(R.id.hidden_id);
                selected_note_id = Integer.parseInt(hidden_id.getText().toString());
                return false;
            }
        });
    }

    public void saveNote(TextView v) {
        Note note = db.insertNote(v.getText().toString());
        values.add(0, note);
        adapter.notifyDataSetChanged();
        v.setText("");
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.add(0, selected_note_id, 0, "Copy");
        menu.add(0, selected_note_id, 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Copy"){
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            Cursor res = db.getNote(item.getItemId());
            ClipData clip = ClipData.newPlainText("note", res.getString(res.getColumnIndex("content")));
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Item copied", Toast.LENGTH_LONG).show();
        }
        if(item.getTitle()=="Delete"){
            db.deleteNote(item.getItemId());
            values.clear();
            values.addAll(db.getAllNotes());
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
