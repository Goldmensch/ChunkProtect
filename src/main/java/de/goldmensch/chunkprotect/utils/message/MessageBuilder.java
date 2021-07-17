package de.goldmensch.chunkprotect.utils.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class MessageBuilder {

    private final TextComponent.Builder builder = Component.empty().toBuilder();

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public MessageBuilder appendLine(Component component) {
        if(!builder.children().isEmpty()) builder.append(Component.text("\n"));
        builder.append(component);
        return this;
    }

    public Component build() {
        return builder.build();
    }

}
