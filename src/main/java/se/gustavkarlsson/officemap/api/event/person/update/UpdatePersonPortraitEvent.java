package se.gustavkarlsson.officemap.api.event.person.update;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import se.gustavkarlsson.officemap.api.item.Person;
import se.gustavkarlsson.officemap.api.item.Sha1;
import se.gustavkarlsson.officemap.api.item.Person.Builder;

@Entity
@Table(name = UpdatePersonPortraitEvent.TYPE)
public final class UpdatePersonPortraitEvent extends UpdatePersonEvent {
	public static final String TYPE = "UpdatePersonPortraitEvent";
	
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "portrait")) })
	private final Sha1 portrait;

	// Required by Hibernate
	private UpdatePersonPortraitEvent() {
		super(0, 0);
		this.portrait = null;
	}
	
	public UpdatePersonPortraitEvent(final long timestamp, final int ref, final Sha1 portrait) {
		super(timestamp, ref);
		this.portrait = portrait;
	}
	
	public final Sha1 getPortrait() {
		return portrait;
	}

	@Override
	protected Person updateProperty(final Builder builder) {
		return builder.withPortrait(portrait).build();
	}

	@Override
	public String toString() {
		return "UpdatePersonPortraitEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", portrait="
				+ portrait + "]";
	}
	
}
