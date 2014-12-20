package se.gustavkarlsson.officemap.events.person.set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.api.Sha1;
import se.gustavkarlsson.officemap.events.person.PersonEvent;

@Entity
@Table(name = SetPersonPortraitEvent.TYPE)
@DiscriminatorValue(SetPersonPortraitEvent.TYPE)
public final class SetPersonPortraitEvent extends PersonEvent {
	public static final String TYPE = "SetPersonPortraitEvent";

	private final Sha1 portrait;
	
	public SetPersonPortraitEvent(final long timestamp, final int personId, final Sha1 portrait) {
		super(timestamp, personId);
		this.portrait = portrait;
	}

	@Override
	public String toString() {
		return "SetPersonPortraitEvent [id=" + id + ", timestamp=" + timestamp + ", personId=" + personId
				+ ", portrait=" + portrait + "]";
	}

}
