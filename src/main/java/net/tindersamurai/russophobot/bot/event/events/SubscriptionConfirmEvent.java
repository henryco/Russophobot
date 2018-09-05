package net.tindersamurai.russophobot.bot.event.events;

import lombok.Value;

@Value public class SubscriptionConfirmEvent {
	private int subscriptionId;
	private boolean confirmed;
}