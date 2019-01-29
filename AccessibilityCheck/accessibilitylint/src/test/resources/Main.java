package test.pkg;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {
    Intent itent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // In a comment, mentioning "lint" has no effect
        String s1 = "Ignore non-word usages: linting";
        String s2 = "Let's say it: lint";

        String url = "http://www.stackoverflow.com";

        Intent i = new Intent(Intent.ACTION_VIEW);
        itent.setData(Uri.parse(url));
        startActivity(i);
    }
    public void accessLink(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setMessage("hushasas").show();

        this.itent = new Intent(Intent.ACTION_VIEW);
        startActivity(itent);
    }
}
