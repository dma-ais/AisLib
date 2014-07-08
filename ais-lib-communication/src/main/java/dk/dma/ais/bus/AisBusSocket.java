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
