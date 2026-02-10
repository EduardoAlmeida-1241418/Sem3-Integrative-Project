package pt.ipp.isep.dei.domain.Tree;

import pt.ipp.isep.dei.domain.Country;
import pt.ipp.isep.dei.domain.TimeZoneGroup;
import java.util.Objects;

public class TZGKey implements Comparable<TZGKey> {

    private final TimeZoneGroup tz;
    private final Country country;

    public TZGKey(TimeZoneGroup tz, Country country) {
        if (tz == null || country == null)
            throw new IllegalArgumentException("TimeZoneGroup and Country cannot be null.");
        this.tz = tz;
        this.country = country;
    }

    public TimeZoneGroup getTz() { return tz; }
    public Country getCountry() { return country; }

    @Override
    public int compareTo(TZGKey o) {
        int cmp = this.tz.compareTo(o.tz);
        if (cmp != 0) return cmp;
        return this.country.compareTo(o.country);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TZGKey)) return false;
        TZGKey other = (TZGKey) o;
        return tz.equals(other.tz) && country.equals(other.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tz, country);
    }

    @Override
    public String toString() {
        return tz + " - " + country;
    }
}
