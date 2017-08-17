package br.com.carregai.carregai2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import br.com.carregai.carregai2.MainActivity;
import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.activity.LoginActivity;
import br.com.carregai.carregai2.activity.SettingsActivity;

public class DrawerUtils implements AccountHeader.OnAccountHeaderProfileImageListener {
    private DrawerBuilder customerDrawer;

    public DrawerBuilder setUpCustomerDrawer
            (Activity activity, String userName, String userAddress, Uri userPhotoUrl, Toolbar toolbar) {

        if (customerDrawer == null) {
            AccountHeader header = new AccountHeaderBuilder()
                    .withActivity(activity)
                    .withOnAccountHeaderProfileImageListener(this)
                    .addProfiles(new ProfileDrawerItem()
                            .withIcon(userPhotoUrl)
                            .withName(userName).withEmail(userAddress)).build();

            customerDrawer = new DrawerBuilder()
                    .withActivity(activity)
                    .withToolbar(toolbar)
                    .withAccountHeader(header)
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName("Home")
                                    .withIcon(R.drawable.ic_home_black),

                            new DividerDrawerItem(),
                            new SecondaryDrawerItem().withName("Configurações")
                                    .withIcon(R.drawable.ic_config),
                            new SecondaryDrawerItem().withName("Sair")
                                    .withIcon(R.drawable.ic_exit));
                    ;
            return customerDrawer;
        } else {
            return customerDrawer;
        }
    }

    @Override
    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {




        return false;
    }

    @Override
    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
        return false;
    }

    public static void onUserClickListener(int position, final Context context, final Activity activity) {

        switch (position) {
            case 1:
                if (context.getClass() != MainActivity.class) {
                    activity.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));
                }
                break;
            case 3:
                if (context.getClass() != SettingsActivity.class)
                    activity.startActivity(new Intent(activity.getBaseContext(), SettingsActivity.class));
                break;
            case 4:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(activity.getBaseContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
                break;
        }
    }
}