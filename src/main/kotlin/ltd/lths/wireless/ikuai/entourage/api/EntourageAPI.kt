package ltd.lths.wireless.ikuai.entourage.api

import ltd.lths.wireless.ikuai.entourage.util.ChatColor

/**
 * ikuai-entourage
 * ltd.lths.wireless.ikuai.entourage.api.EntourageAPI
 *
 * @author Score2
 * @since 2022/06/08 1:11
 */

val String.ansiColored get() = ChatColor.toANSI(this)