# Wyfy Suite

## 🎯 Vision

The Wyfy Suite transforms Minecraft automation from imperative "how" to declarative "what" - inspired by NixOS philosophy. Instead of building complex redstone circuits and mechanical contraptions, players write simple expressions that describe their desired outcomes.

## 📦 Architecture

### **LibWyfy** - Core Framework
*The foundation library powering all Wyfy mods*

**Key Features:**
- **Energy System**: A non RF/FE energy system with Create integrations
- **Expression Language**: Declarative code for automation/building/storage/infrastructure
- **Multiblock Framework**: A powerful scalable option to fulfill numerous needs
- **Platform Abstraction**: Cross-platform compatibility (Fabric/NeoForge/Forge)

**Energy Conversion/Logic:**
```
Available WE = Total SU × Conversion Rate
Machine Speed = Base Performance(voltage) × RPM
```

### **IvaldiOS** - Smart Automation
*The main automation mod with intelligent power management*

**Revolutionary Power Control:**
- **Software Overclocking**: Adjust machine speed through configuration, not mechanical builds
- **Dynamic SU Consumption**: Higher performance = higher stress unit usage
- **No Gear Ratio Requirements**: Speed control without complex mechanical engineering

**Example: Advanced Smeltery**
```yaml
# Traditional Create: Build gear ratios for speed
Water Wheels → Large Cog → Small Cog → Faster Machine

# IvaldiOS: Configure performance settings  
Performance Level: 200%    # 2x speed
SU Consumption: 400%       # 4x power usage
```

**Core Machines:**
- **Smart Furnaces**: Configurable speed vs power consumption
- **Adaptive Processors**: Auto-adjust based on throughput needs
- **Resource Analyzers**: Monitor and optimize power efficiency
- **Builder**: A block adding building functionality with multiblock structures.

### **DraupnirTech** - Advanced Energy
*Nuclear and high-tech power systems*

**Advanced Energy Generation:**
- **Nuclear Reactors**: Massive WE generation with safety mechanics
- **Power Distribution**: Long-range energy transmission


### Tech Mod structure
**LibWyfy** (required foundation)
**IvaldiOS** for smart automation
**DraupnirTech** Gregified obscure nuclear power

### Your First Smart Machine

**Traditional Create Approach:**
```
Build Smeltery → Connect to Create network → 
```

**IvaldiOS Approach:**
```nix
// Write an expression file
{
  description: "Post mining ore processing";
  
  inputs = {
    oak_chest = {
      items = {
        raw_iron_ore = {};
        raw_steak = {};
        sand = {};
      };
    };
    advanced_fluid_tank = {
      fluid = {
        lava = {};
      };
    };
    catalytic_bushing = {
      rpm = 64;
    };
    
  };
  
  outputs = {
    iron_chest = {
      items = {
        iron_ingot = {};
        cooked_steak = {};
        glass = {};
      };
    };
  };
  
  performance = {
    
  };
}
```

The machine automatically:
- Calculates optimal SU consumption (384 SU for 150% performance)
- Adjusts internal timing without mechanical changes
- Provides feedback through Create's stress system

## 🎮 Player Experience

### Power Management Philosophy

**IvaldiOS eliminates the tedium of mechanical power scaling:**

**New Way**: *"I need my smeltery to run faster... let me increase the performance setting to 200%"*

**Benefits:**
- **Flexibility**: Adjust performance on-the-fly
- **Efficiency**: No mechanical rebuilds required
- **Scalability**: Easy to upgrade existing setups
- **Integration**: Works seamlessly with existing Create networks

### Example Progression

**Early Game:**
```yaml
Basic Smeltery:
  Performance: 100% (standard speed)
  SU Usage: 256 SU
  Requirements: 2 water wheels
```

**Mid Game:**  
```yaml
Upgraded Smeltery:
  Performance: 200% (double speed)
  SU Usage: 512 SU  
  Requirements: 4 water wheels (or better generator)
```

**Late Game:**
```yaml
Optimized Smeltery:
  Performance: 300% (triple speed)
  SU Usage: 768 SU
  Requirements: DraupnirTech reactor
  Features: Auto-throttling, efficiency monitoring
```

## 🔧 Technical Design

### Energy System Architecture

**LibWyfy Energy Conversion:**
```java
// SU is the primary resource (like Create)
base_we = available_su * SU_TO_WE_RATIO;

// Performance settings affect consumption
performance_multiplier = user_setting / 100.0;  // 200% = 2.0x
required_we = base_consumption * performance_multiplier;

// Machine operates if sufficient power
can_operate = base_we >= required_we;
```

**IvaldiOS Machine Control:**
```java
// Player sets desired performance level
public void setPerformanceLevel(int percentage) {
    this.performanceLevel = percentage;
    this.suConsumption = baseSU * (percentage / 100.0);
    this.processingSpeed = baseSpeed * (percentage / 100.0);
}
```

### Cross-Platform Compatibility

Built with **multiloader architecture**:
- **Fabric**: Lightweight, modern
- **NeoForge**: Feature-rich, stable  
- **Forge**: Legacy compatibility

## 🏗️ Development Status

### LibWyfy (Foundation) ✅
- [x] Basic energy system framework
- [x] Constants and initialization
- [x] Platform abstraction layer
- [ ] Expression language compiler
- [ ] Multiblock registry system

### IvaldiOS (Automation) 🚧
- [ ] Smart machine base classes
- [ ] Performance configuration system
- [ ] Dynamic SU consumption
- [ ] Expression-driven automation
- [ ] GUI for machine settings

### DraupnirTech (Nuclear) 📋
- [ ] Reactor framework
- [ ] Advanced energy generation
- [ ] Safety systems
- [ ] Power distribution network

## 🎯 Goals

### Short Term
1. **Complete LibWyfy energy system**
2. **Implement basic IvaldiOS machine** with performance controls
3. **Create comprehensive documentation**

### Long Term  
1. **Full expression language** for declarative automation
2. **Advanced nuclear systems** in DraupnirTech
3. **Integration with popular Create addons**
4. **Community-driven machine definitions**

## 🤝 Contributing

- **Reproducible builds** via Nix flakes
- **Cross-platform testing** on all supported loaders  
- **Expression-driven configuration** instead of imperative code

### Development Setup
```bash
# Clone and enter Nix shell
git clone https://github.com/wyatt/wyfy-suite
cd wyfy-suite
nix develop

# Build all modules  
./gradlew build

# Test specific platform
./gradlew :libwyfy:fabric:build
```

## 📄 License

MIT License - See LICENSE file for details.

## 🌟 Inspiration

*"If NixOS is declarative system configuration, then Wyfy Suite is declarative Minecraft automation."*

**Influenced by:**
- **NixOS**: Declarative system management
- **Create**: Mechanical engineering in Minecraft  
- **Applied Energistics**: Intelligent automation
- **Norse Mythology**: Naming conventions and theming
