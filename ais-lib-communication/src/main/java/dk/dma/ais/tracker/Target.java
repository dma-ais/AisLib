/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.dma.ais.tracker;

import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

@Immutable
public abstract class Target implements Serializable {

    protected Target(int mmsi) {
        this.mmsi = mmsi;
    }

    /**
     * Returns the MMSI of the Track.
     * @return the MMSI of the Track
     */
    public int getMmsi() {
        return mmsi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Target target = (Target) o;
        return mmsi == target.mmsi;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Target{");
        sb.append("mmsi=").append(mmsi);
        sb.append('}');
        return sb.toString();
    }

    private final int mmsi;
}
