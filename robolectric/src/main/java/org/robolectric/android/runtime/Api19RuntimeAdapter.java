package org.robolectric.android.runtime;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.ViewRootImpl;

import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.fakes.RoboInstrumentation;
import org.robolectric.util.ReflectionHelpers;
import org.robolectric.util.ReflectionHelpers.ClassParameter;

public class Api19RuntimeAdapter implements RuntimeAdapter {

  @Override
  public void callActivityAttach(Object component, Context baseContext, Class<?> activityThreadClass, Application application, Intent intent, ActivityInfo activityInfo, String activityTitle, Class<?> nonConfigurationInstancesClass) {
    ReflectionHelpers.callInstanceMethod(Activity.class, component, "attach",
        ClassParameter.from(Context.class, baseContext),
        ClassParameter.from(activityThreadClass, RuntimeEnvironment.getActivityThread()),
        ClassParameter.from(Instrumentation.class, new RoboInstrumentation()),
        ClassParameter.from(IBinder.class, null),
        ClassParameter.from(int.class, 0),
        ClassParameter.from(Application.class, application),
        ClassParameter.from(Intent.class, intent),
        ClassParameter.from(ActivityInfo.class, activityInfo),
        ClassParameter.from(CharSequence.class, activityTitle),
        ClassParameter.from(Activity.class, null),
        ClassParameter.from(String.class, "id"),
        ClassParameter.from(nonConfigurationInstancesClass, null),
        ClassParameter.from(Configuration.class, application.getResources().getConfiguration()));
  }

  @Override
  public void callViewRootImplDispatchResized(Object component, Rect frame, Rect overscanInsets,
      Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw,
      Configuration newConfig) {
    ReflectionHelpers.callInstanceMethod(ViewRootImpl.class, component, "dispatchResized",
        ClassParameter.from(Rect.class, frame),
        ClassParameter.from(Rect.class, overscanInsets),
        ClassParameter.from(Rect.class, contentInsets),
        ClassParameter.from(Rect.class, visibleInsets),
        ClassParameter.from(boolean.class, reportDraw),
        ClassParameter.from(Configuration.class, newConfig));
  }
}
