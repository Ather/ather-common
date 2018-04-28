package com.atherapp.common.util;

public class DualKey {
	public Object key1, key2;

	public DualKey(Object key1, Object key2) {
		this.key1 = key1;
		this.key2 = key2;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DualKey))
			return false;
		DualKey ref = (DualKey) obj;
		return this.key1.equals(ref.key1) &&
				this.key2.equals(ref.key2);
	}

	@Override
	public int hashCode() {
		return key1.hashCode() ^ key2.hashCode();
	}
}
