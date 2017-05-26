package com.xfinity.resourceprovider.sample;

import android.content.Context;
import java.lang.Object;
import java.lang.String;

public class ResourceProvider {
  private Context context;

  public ResourceProvider(Context context) {
    this.context = context;
  }

  public String getAbcActionBarHomeDescription(Object... formatArgs) {
    return context.getString(R.string.abc_action_bar_home_description, formatArgs);
  }

  public String getAbcActionBarHomeDescriptionFormat(Object... formatArgs) {
    return context.getString(R.string.abc_action_bar_home_description_format, formatArgs);
  }

  public String getAbcActionBarHomeSubtitleDescriptionFormat(Object... formatArgs) {
    return context.getString(R.string.abc_action_bar_home_subtitle_description_format, formatArgs);
  }

  public String getAbcActionBarUpDescription(Object... formatArgs) {
    return context.getString(R.string.abc_action_bar_up_description, formatArgs);
  }

  public String getAbcActionMenuOverflowDescription(Object... formatArgs) {
    return context.getString(R.string.abc_action_menu_overflow_description, formatArgs);
  }

  public String getAbcActionModeDone(Object... formatArgs) {
    return context.getString(R.string.abc_action_mode_done, formatArgs);
  }

  public String getAbcActivityChooserViewSeeAll(Object... formatArgs) {
    return context.getString(R.string.abc_activity_chooser_view_see_all, formatArgs);
  }

  public String getAbcActivitychooserviewChooseApplication(Object... formatArgs) {
    return context.getString(R.string.abc_activitychooserview_choose_application, formatArgs);
  }

  public String getAbcCapitalOff(Object... formatArgs) {
    return context.getString(R.string.abc_capital_off, formatArgs);
  }

  public String getAbcCapitalOn(Object... formatArgs) {
    return context.getString(R.string.abc_capital_on, formatArgs);
  }

  public String getAbcFontFamilyBody1Material(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_body_1_material, formatArgs);
  }

  public String getAbcFontFamilyBody2Material(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_body_2_material, formatArgs);
  }

  public String getAbcFontFamilyButtonMaterial(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_button_material, formatArgs);
  }

  public String getAbcFontFamilyCaptionMaterial(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_caption_material, formatArgs);
  }

  public String getAbcFontFamilyDisplay1Material(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_display_1_material, formatArgs);
  }

  public String getAbcFontFamilyDisplay2Material(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_display_2_material, formatArgs);
  }

  public String getAbcFontFamilyDisplay3Material(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_display_3_material, formatArgs);
  }

  public String getAbcFontFamilyDisplay4Material(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_display_4_material, formatArgs);
  }

  public String getAbcFontFamilyHeadlineMaterial(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_headline_material, formatArgs);
  }

  public String getAbcFontFamilyMenuMaterial(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_menu_material, formatArgs);
  }

  public String getAbcFontFamilySubheadMaterial(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_subhead_material, formatArgs);
  }

  public String getAbcFontFamilyTitleMaterial(Object... formatArgs) {
    return context.getString(R.string.abc_font_family_title_material, formatArgs);
  }

  public String getAbcSearchHint(Object... formatArgs) {
    return context.getString(R.string.abc_search_hint, formatArgs);
  }

  public String getAbcSearchviewDescriptionClear(Object... formatArgs) {
    return context.getString(R.string.abc_searchview_description_clear, formatArgs);
  }

  public String getAbcSearchviewDescriptionQuery(Object... formatArgs) {
    return context.getString(R.string.abc_searchview_description_query, formatArgs);
  }

  public String getAbcSearchviewDescriptionSearch(Object... formatArgs) {
    return context.getString(R.string.abc_searchview_description_search, formatArgs);
  }

  public String getAbcSearchviewDescriptionSubmit(Object... formatArgs) {
    return context.getString(R.string.abc_searchview_description_submit, formatArgs);
  }

  public String getAbcSearchviewDescriptionVoice(Object... formatArgs) {
    return context.getString(R.string.abc_searchview_description_voice, formatArgs);
  }

  public String getAbcShareactionproviderShareWith(Object... formatArgs) {
    return context.getString(R.string.abc_shareactionprovider_share_with, formatArgs);
  }

  public String getAbcShareactionproviderShareWithApplication(Object... formatArgs) {
    return context.getString(R.string.abc_shareactionprovider_share_with_application, formatArgs);
  }

  public String getAbcToolbarCollapseDescription(Object... formatArgs) {
    return context.getString(R.string.abc_toolbar_collapse_description, formatArgs);
  }

  public String getActivityA(Object... formatArgs) {
    return context.getString(R.string.activity_a, formatArgs);
  }

  public String getActivityB(Object... formatArgs) {
    return context.getString(R.string.activity_b, formatArgs);
  }

  public String getAppName(Object... formatArgs) {
    return context.getString(R.string.app_name, formatArgs);
  }

  public String getFirstHalfOfMonth(Object... formatArgs) {
    return context.getString(R.string.first_half_of_month, formatArgs);
  }

  public String getOneArgFormattedString(Object... formatArgs) {
    return context.getString(R.string.one_arg_formatted_string, formatArgs);
  }

  public String getSearchMenuTitle(Object... formatArgs) {
    return context.getString(R.string.search_menu_title, formatArgs);
  }

  public String getSecondHalfOfMonth(Object... formatArgs) {
    return context.getString(R.string.second_half_of_month, formatArgs);
  }

  public String getStatusBarNotificationInfoOverflow(Object... formatArgs) {
    return context.getString(R.string.status_bar_notification_info_overflow, formatArgs);
  }
}
