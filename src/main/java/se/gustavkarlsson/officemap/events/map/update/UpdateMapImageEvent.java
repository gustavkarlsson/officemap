package se.gustavkarlsson.officemap.events.map.update;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import se.gustavkarlsson.officemap.api.items.Map;
import se.gustavkarlsson.officemap.api.items.Sha1;
import se.gustavkarlsson.officemap.api.items.Map.MapBuilder;

@Entity
@Table(name = UpdateMapImageEvent.TYPE)
public final class UpdateMapImageEvent extends UpdateMapEvent {
	public static final String TYPE = "UpdateMapImageEvent";
	
	@NotNull
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "image", nullable = false)) })
	private final Sha1 image;
	
	// Required by Hibernate
	private UpdateMapImageEvent() {
		super(0, 0);
		this.image = null;
	}
	
	public UpdateMapImageEvent(final long timestamp, final int ref, final Sha1 imageSha1) {
		super(timestamp, ref);
		this.image = imageSha1;
	}
	
	public final Sha1 getImage() {
		return image;
	}
	
	@Override
	protected Map updateProperty(final MapBuilder builder) {
		return builder.withImage(image).build();
	}
	
	@Override
	public String toString() {
		return "UpdateMapImageEvent [id=" + id + ", timestamp=" + timestamp + ", ref=" + ref + ", image=" + image + "]";
	}
	
}
