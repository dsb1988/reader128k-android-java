package net.reader128k.ui;

import android.app.Activity;
import net.reader128k.domain.*;
import net.reader128k.interfaces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseReaderActivity extends Activity
        implements OnTreeNodeSelectListener, ReaderObserver {

    @Override
    public void onConnect() {}

    @Override
    public void onUserInfo(User user) {}

    @Override
    public void onTree(List<TreeNode> tree) {}

    @Override
    public void onPosts(List<Post> posts) {}

    @Override
    public void onLogin(LoginResponse response) {}

    @Override
    public void onLogout(LogoutResponse response) {}

    @Override
    public void onTreeNodeSelected(int position) {}
}