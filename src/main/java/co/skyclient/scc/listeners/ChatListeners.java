/*
 * SkyclientCosmetics - Cool cosmetics for a mod installer Skyclient!
 * Copyright (C) koxx12-dev [2021 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found under this url
 * https://github.com/koxx12-dev/Skyclient-Cosmetics
 *
 * If you have a private concern, please contact me on
 * Discord: Koxx12#8061
 */

package co.skyclient.scc.listeners;

import cc.woverflow.onecore.utils.InternetUtils;
import cc.woverflow.onecore.utils.JsonUtils;
import co.skyclient.scc.config.Settings;
import co.skyclient.scc.cosmetics.Tag;
import co.skyclient.scc.cosmetics.TagCosmetics;
import co.skyclient.scc.utils.ChatUtils;
import com.google.gson.JsonObject;
import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;
import gg.essential.universal.ChatColor;
import co.skyclient.scc.SkyclientCosmetics;
import co.skyclient.scc.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListeners {

    public static Pattern hypixelAPIKeyMsgRegex = Pattern.compile("Your new API key is [0-9a-z]{8}-([0-9a-z]{4}-){3}[0-9a-z]{12}");

    public static Pattern chatRegex = Pattern.compile("((To|From)\\s)?((Guild|Co-op|Officer|Party)\\s\\>\\s)?(\\[(MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|MOJANG|SLOTH|EVENTS|MCP)([\\+]{1,2})?\\]\\s)?[\\w]+:\\s.*");
    public static Pattern dmRegex = Pattern.compile("((To|From)\\s)(\\[(MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|MOJANG|SLOTH|EVENTS|MCP)([\\+]{1,2})?\\]\\s)?[\\w]+:\\s.*");
    public static Pattern groupRegex = Pattern.compile("((Guild|Co-op|Officer|Party)\\s\\>\\s)(\\[(MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|MOJANG|SLOTH|EVENTS|MCP)([\\+]{1,2})?\\]\\s)?[\\w]+:\\s.*");
    public static Pattern rankRegex = Pattern.compile("\\[(MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|MOJANG|SLOTH|EVENTS|MCP)([\\+]{1,2})?\\]");
    public static Pattern groupRankRegex = Pattern.compile("((\u00A7[a-f0-9kmolnr](To|From))|(\u00A7[a-f0-9kmolnr](Guild|Co-op|Officer|Party)\\s(\u00A7[a-f0-9kmolnr])?\\\\u003e))\\s((\u00A7[a-f0-9kmolnr])?(\\[(MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|MOJANG|SLOTH|EVENTS|MCP)([\\+]{1,2})?\\]\\s)?)?[\\w]+(\u00A7[a-f0-9kmolnr])?:");

    @SubscribeEvent
    public void onChatMsgTags(ClientChatReceivedEvent event) {
        if (Settings.showTags) {
            try {
                if (event.type == 0) {
                    String cleanMessage = StringUtils.cleanMessage(event.message.getUnformattedText());
                    List<String> splitMessage = Arrays.asList(cleanMessage.split("\\s"));
                    String parsedMessage = IChatComponent.Serializer.componentToJson(event.message);
                    Matcher parsedMatcher = groupRankRegex.matcher(parsedMessage);
                    String playerName;
                    if (chatRegex.matcher(cleanMessage).matches()) {
                        if (dmRegex.matcher(cleanMessage).matches()) {
                            JsonObject jsonParsedMsg = JsonUtils.asJsonElement(parsedMessage).getAsJsonObject();
                            String playerText = jsonParsedMsg.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("text").getAsString();
                            String playerColor;
                            try {
                                playerColor = ChatColor.valueOf(jsonParsedMsg.get("extra").getAsJsonArray().get(0).getAsJsonObject().get("color").getAsString().toUpperCase()).toString();
                            } catch (Exception e) {
                                playerColor = ChatColor.GRAY.toString();
                            }

                            playerName = playerText.replaceAll(rankRegex.pattern(),"").trim();

                            Tag tag = TagCosmetics.getInstance().getTag(playerName);
                            if (tag != null) {
                                String newVal = tag.getFullTag() + " " + playerColor+playerText;
                                jsonParsedMsg.get("extra").getAsJsonArray().get(0).getAsJsonObject().addProperty("text",newVal);
                                event.message = IChatComponent.Serializer.jsonToComponent(jsonParsedMsg.toString());
                            }
                        } else if (groupRegex.matcher(cleanMessage).matches()) {
                            if (parsedMatcher.find()) {
                                String msg = parsedMatcher.group(0);
                                List<String> msgList = new ArrayList<>(Arrays.asList(msg.split(" ")));
                                List<String> cleanMsg = Arrays.asList(StringUtils.cleanMessage(msg).split(" "));
                                playerName = cleanMsg.get(cleanMsg.size()-1).replaceAll(":","");
                                Tag tag = TagCosmetics.getInstance().getTag(playerName);
                                if (tag != null) {
                                    msgList.add(2, tag.getFullTag());
                                    String newVal = String.join(" ", msgList);
                                    event.message = IChatComponent.Serializer.jsonToComponent(parsedMessage.replace(msg,newVal));
                                }
                            }
                        } else {
                            if (rankRegex.matcher(splitMessage.get(0)).matches()) {
                                playerName = splitMessage.get(1);
                            } else {
                                playerName = splitMessage.get(0);
                            }
                            playerName = playerName.replaceAll(":","");
                            Tag tag = TagCosmetics.getInstance().getTag(playerName);
                            if (tag != null) {
                                event.message = new ChatComponentText(tag.getFullTag() + " ").appendSibling(event.message);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onChatMsgHpApi(ClientChatReceivedEvent event) {
        if (event.type == 0) {
            String msg = StringUtils.cleanMessage(event.message.getUnformattedText());
            if (msg.contains("Your new API key is ")) {
                Matcher match = hypixelAPIKeyMsgRegex.matcher(msg);
                if (match.find()) {
                    String cleanMsg = match.group();
                    String key = cleanMsg.replace("Your new API key is ", "");
                    SkyclientCosmetics.LOGGER.info(key);
                    ChatUtils.sendSystemMessage(ChatColor.GREEN + "Checking API key");
                    Multithreading.runAsync(() -> {
                        try {
                            JsonObject response = InternetUtils.fetchJsonElement(WebUtil.INSTANCE, "https://api.hypixel.net/key?key=" + key).getAsJsonObject();

                            if (response.get("success").getAsBoolean() && response.get("record").getAsJsonObject().get("owner").getAsString().replaceAll("-", "").equals(Minecraft.getMinecraft().getSession().getPlayerID())) {
                                ChatUtils.sendSystemMessage(ChatColor.GREEN + "Verified API key!");
                                Settings.hpApiKey = key;
                                SkyclientCosmetics.config.markDirty();
                                SkyclientCosmetics.config.writeData();
                            } else {
                                ChatUtils.sendSystemMessage(ChatColor.RED + "Couldn't verify \"" + key + "\" as a API key");
                            }

                        } catch (Exception e) {
                            ChatUtils.sendSystemMessage(ChatColor.RED + "\"" + key + "\" is not a valid API key");
                        }
                    });
                }
            }
        }
    }
}
