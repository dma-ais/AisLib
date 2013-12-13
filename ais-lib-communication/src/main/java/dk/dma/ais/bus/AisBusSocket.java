/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.ais.bus;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * Base class for consumers and providers connected to the bus
 */
@ThreadSafe
public abstract class AisBusSocket extends AisBusComponent {

    @GuardedBy("this")
    private AisBus aisBus;
    @GuardedBy("this")
    private String name;
    @GuardedBy("this")
    private String description;
    
    protected final boolean blocking;
    
    public AisBusSocket() {
        this(false);
    }
    
    public AisBusSocket(boolean blocking) {
        super();
        this.blocking = blocking;
    }
    
    @Override
    public void init() {
        super.init();
    }
    
    @Override
    public void start() {
        super.start();
    }

    public synchronized AisBus getAisBus() {
        return aisBus;
    }
    
    public synchronized void setAisBus(AisBus aisBus) {
        if (this.aisBus != null) {
            throw new IllegalStateException("AisBus already defined");
        }
        this.aisBus = aisBus;
    }

    public synchronized String getName() {
        return name == null ? getClass().getSimpleName() : name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized String getDescription() {
        return description;
    }

    public synchronized void setDescription(String description) {
        this.description = description;
    }
    
}
