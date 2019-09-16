/*
 * Copyright 2016 Universidade Federal do Rio Grande do Sul
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cpd.utils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cpd.ufrgsmobile.R;
import com.jawnnypoo.physicslayout.Physics;
import com.jawnnypoo.physicslayout.PhysicsConfig;
import com.jawnnypoo.physicslayout.PhysicsLinearLayout;
import com.jawnnypoo.physicslayout.PhysicsRelativeLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import io.kimo.konamicode.KonamiCode;
import io.kimo.konamicode.KonamiCodeLayout;

public class AppRegister extends  AppCompatActivity {

    public static void register(final AppCompatActivity activity){
    new KonamiCode.Installer(activity)
    .on(activity)
    .callback(new KonamiCodeLayout.Callback() {
    @Override
    public void onFinish() {
    Intent i = new Intent(activity, AppRegister.class);
    activity.startActivity(i);}}).install();}
    int UF_EE = 0;
    int AW_EE = 1;
    int TM_EE = 2;
    int PHYSICS = 3;
    int TXT = 4;
    int CR_EE = 5;
    int AC_EE = 6;
    PhysicsLinearLayout layout;
    CircleImageView splash;
    CircleImageView aw;
    CircleImageView tm;
    CircleImageView cr;
    CircleImageView ac;
    TextView txt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PhysicsLinearLayout prl = new PhysicsLinearLayout(this);
    prl.setId(PHYSICS);
    TrackerUtils.INSTANCE.registerEnter();
    RelativeLayout.LayoutParams prllp =
    new PhysicsRelativeLayout.LayoutParams(
    RelativeLayout.LayoutParams.MATCH_PARENT,
    RelativeLayout.LayoutParams.MATCH_PARENT
    );
    prl.setLayoutParams(prllp);
    prl.setBackgroundColor(ContextCompat.getColor(this, R.color.mainRed));
    CircleImageView civ1 = new CircleImageView(this);
    civ1.setId(UF_EE);
    civ1.setVisibility(View.VISIBLE);
    int sizePx = MeasureUtils.INSTANCE.convertDpToPixel(100, this);
    PhysicsRelativeLayout.LayoutParams civ1lp = new PhysicsRelativeLayout.LayoutParams(sizePx, sizePx);
    civ1.setLayoutParams(civ1lp);
    civ1.setBorderColor(ContextCompat.getColor(this, R.color.white));
    civ1.setImageResource(R.drawable.ic_launcher_splash);
    prl.addView(civ1);
    CircleImageView civ2 = new CircleImageView(this);
    civ2.setId(AW_EE);
    civ2.setVisibility(View.VISIBLE);
    sizePx = MeasureUtils.INSTANCE.convertDpToPixel(70, this);
    PhysicsRelativeLayout.LayoutParams civ2lp = new PhysicsRelativeLayout.LayoutParams(sizePx, sizePx);
    civ2.setLayoutParams(civ2lp);
    civ2.setBorderColor(ContextCompat.getColor(this, R.color.white));
    civ2.setImageResource(R.drawable.ic_launcher_splash);
    prl.addView(civ2);
    CircleImageView civ3 = new CircleImageView(this);
    civ3.setId(TM_EE);
    civ3.setVisibility(View.VISIBLE);
    PhysicsRelativeLayout.LayoutParams civ3lp = new PhysicsRelativeLayout.LayoutParams(sizePx, sizePx);
    civ3.setLayoutParams(civ3lp);
    civ3.setBorderColor(ContextCompat.getColor(this, R.color.white));
    civ3.setImageResource(R.drawable.ic_launcher_splash);
    prl.addView(civ3);
    CircleImageView civ4 = new CircleImageView(this);
    civ4.setId(CR_EE);
    civ4.setVisibility(View.VISIBLE);
    PhysicsRelativeLayout.LayoutParams civ4lp = new PhysicsRelativeLayout.LayoutParams(sizePx, sizePx);
    civ4.setLayoutParams(civ4lp);
    civ4.setBorderColor(ContextCompat.getColor(this, R.color.white));
    civ4.setImageResource(R.drawable.ic_launcher_splash);
    prl.addView(civ4);
    CircleImageView civ5 = new CircleImageView(this);
    civ5.setId(AC_EE);
    civ5.setVisibility(View.VISIBLE);
    PhysicsRelativeLayout.LayoutParams civ5lp = new PhysicsRelativeLayout.LayoutParams(sizePx, sizePx);
    civ5.setLayoutParams(civ5lp);
    civ5.setBorderColor(ContextCompat.getColor(this, R.color.white));
    civ5.setImageResource(R.drawable.ic_launcher_splash);
    prl.addView(civ5);
    View view = prl;
    setContentView(view);
    splash = (CircleImageView) view.findViewById(UF_EE);
    aw = (CircleImageView) view.findViewById(AW_EE);
    tm = (CircleImageView) view.findViewById(TM_EE);
    ac = (CircleImageView) view.findViewById(AC_EE);
    cr = (CircleImageView) view.findViewById(CR_EE);
    layout = (PhysicsLinearLayout) view.findViewById(PHYSICS);
    //txt = (TextView) view.findViewById(TXT);
    TextDrawable drawable1 = TextDrawable.builder()
    .beginConfig()
    .textColor(Color.rgb(0xe3, 0x45, 0x40))
    .width(300)  // width in px
    .height(300) // height in px
    .bold()
    .endConfig()
    .buildRect("AW", Color.WHITE);
    aw.setImageDrawable(drawable1);
    TextDrawable drawable2 = TextDrawable.builder()
    .beginConfig()
    .textColor(Color.rgb(0xe3, 0x45, 0x40))
    .width(300)  // width in px
    .height(300) // height in px
    .bold()
    .endConfig()
    .buildRect("TM", Color.WHITE);
    tm.setImageDrawable(drawable2);
    PhysicsConfig config = new PhysicsConfig.Builder()
    .setShapeType(PhysicsConfig.ShapeType.CIRCLE)
    .setDensity(0.6f)
    .setFriction(0.4f)
    .setRestitution(0.4f)
    .build();
    TextDrawable drawable3 = TextDrawable.builder()
            .beginConfig()
            .textColor(Color.rgb(0xe3, 0x45, 0x40))
            .width(300)  // width in px
            .height(300) // height in px
            .bold()
            .endConfig()
            .buildRect("CR", Color.WHITE);
    cr.setImageDrawable(drawable3);
    TextDrawable drawable4 = TextDrawable.builder()
            .beginConfig()
            .textColor(Color.rgb(0xe3, 0x45, 0x40))
            .width(300)  // width in px
            .height(300) // height in px
            .bold()
            .endConfig()
            .buildRect("AC", Color.WHITE);
    ac.setImageDrawable(drawable4);
    Physics.setPhysicsConfig(splash, config);
    Physics.setPhysicsConfig(aw, config);
    Physics.setPhysicsConfig(tm, config);
    Physics.setPhysicsConfig(cr, config);
    Physics.setPhysicsConfig(ac, config);
    layout.getPhysics().enableFling();
    }

}
