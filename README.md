# Boosters
Introduces Boosters for plugins such as ShopGUIPlus, GriefPrevention, McMMO, Factions
<br>
Version: `1.0.9-Release` <br>
Native API: `Paper-1.16.5-R0.1-SNAPSHOT` <br>
Source Code: <a href="https://github.com/Splice-Games/Boosters">github.com/Splice-Games/Boosters</a> <br>
Developer: `Frcsty` <br>

## How To (Server Owner)
This is a plugin built on PaperAPI, and is required to properly run this plugin.

<b>Installation:</b> <br>
| Place SGBoosters-VERSION.jar (`SGBoosters-1.0.9-Release.jar`) file into the plugins folder. <br>
| Start the server, plugin will generate `SGBoosters` directory with files:
* `config.yml`
* `menu/booster-list-menu.yml` <br>
* `booster-storage.yml` (temporary file for persistency) <br>

| Stop the server after everything has been loaded. <br>
| Open and configure the plugin to your needs. <br>
| Start the server and enjoy the plugin!

## Commands
The plugin provides two commands to operate it. <br>

- `/booster` (`booster give <target> <type> <booster-target> <magnitude> <duration> <preset>`) <br>
  Grants a booster voucher of a specific type to the targeted player <br>
  ie. `/booster give Frcsty SELL ALL 1.035 3600 rare` <br>
  - Which would grant the target a booster voucher of type `SELL`
    <br>
    with a scope of `GLOBAL`, magnitude of `1.035` and a duration of `1 Hour`.

  <br>

- `/boosters` <br>
  Opens a menu which displays all active boosters applicable to the viewing user <br>
  - Actions <br>
    The menu provides basic actions such as `CLOSE`, `NEXT_PAGE`, `PREVIOUS_PAGE`,
    and a booster item specific action which allows the booster owner to cancel a booster
    
## Types
The plugin as of right now provides `7` different Booster types, however is designed in a way <br> 
that more can easily be added in the future. <br>
Display format: `BoosterType (Required Plugin)`

- `SHOP_GUI_PLUS_SELL` (ShopGUIPlus) <br>
    *Description:* Is triggered when a user successfully sells items to the shop, <br>
    multiplies the gained amount of money with the given booster magnitude.


- `SHOP_GUI_PLUS_DISCOUNT` (ShopGUIPlus) <br>
    *Description:* Is triggered when a user successfully purchases items from the shop, <br>
    refunds a percentage of the purchase cost depending on the booster magnitude.
  

- `MCMMO_GAIN` (McMMO) <br>
    *Description:* Multiplies the gained experience when receiving McMMO experience, <br>
    by the given booster magnitude.


- `EXPERIENCE_GAIN` (None) <br>
    *Description:* Multiplies the gained experience by the given booster magnitude <br>
    (vanilla experience).
  

- `CROP_GROWTH` (GriefPrevention) <br>
    *Description:* Speeds up crop growth within player claims by the given booster magnitude, <br>
    from the amount of active boosters: `1 Booster -> 2x, 2 Boosters -> 3x`
  

- `MOB_DROPS` (GriefPrevention) <br>
    *Description:* Increases the mob drops gained from mobs within claims depending on the <br>
    given booster magnitude: `< 1.15 -> +1, > 1.5 -> +2` 
  

- `BLOCK_BREAK` (None) <br>
    *Description:* Multiplies mined blocks if a certain chance (`magnitude * 1000 <= 75`) <br>
    is met. Only increases by 1 block. (The lower the magnitude, the higher the chance).
  
## Configuration
The plugin's default configuration (`config.yml`)
```yaml
message:
  invalid-booster-type: [
      "[&c&l| &cInvalid Booster!](hover: &fAvailable Types: &r\n%boosters_booster-types_&b&l\\| &7{type}&f, \n%)"
  ]
  un-applicable-booster-type: [
      "[&c&l| &cUn-Applicable Booster!](hover: &fA booster of this type is already active!)"
  ]
  activated-booster: [
      "[&a&l| &aActivated Booster!](hover: &fSuccessfully activated a &b{formatted-type} &fBooster\n&fwith the duration of &b{duration}&f,\n&fmagnitude of &b{magnitude}x&f, and a &b{scope} &fscope.)"
  ]
  received-booster-voucher: [
      "[&e&l| &eReceived Booster Voucher!](hover: &fYou have been given a &b{formatted-type} &fBooster Voucher.)"
  ]
  active-boosters: [
      "[&b&l| &bActive Boosters](hover: &fClick to view all active boosters)"
  ]
  booster-expired: [
      "[&c&l| &cBooster Expired!](hover: &b{formatted-type} &fBooster expired.)"
  ]
  cancelled-booster: [
      "[&c&l| &cCancelled Booster!](hover: &b{formatted-type} &fBooster has been cancelled!)"
  ]

booster-message:
  shop-sell-message: [
      "[&a&l| &aSell Booster Bonus! &7(+${amount})](hover: &b&l\\| &fOwner: &b{owner}\n&b&l\\| &fDuration: &b{duration}\n&b&l\\| &fMagnitude: &b{magnitude}x)"
  ]
  shop-buy-message: [
      "[&a&l| &aDiscount Booster Bonus! &7(+${amount})](hover: &b&l\\| &fOwner: &b{owner}\n&b&l\\| &fDuration: &b{duration}\n&b&l\\| &fMagnitude: &b{magnitude}x)"
  ]
  mcmmo-experience-gain-message: [
      "[&a&l| &aMcMMO Gain Booster Bonus! &7(+{amount} XP)](hover: &b&l\\| &fOwner: &b{owner}\n&b&l\\| &fDuration: &b{duration}\n&b&l\\| &fMagnitude: &b{magnitude}x)"
  ]
  experience-gain-message: [
      "[&a&l| &aExperience Gain Booster Bonus! &7(+{amount} XP)](hover: &b&l\\| &fOwner: &b{owner}\n&b&l\\| &fDuration: &b{duration}\n&b&l\\| &fMagnitude: &b{magnitude}x)"
  ]

booster-descriptions:
  SHOP_GUI_PLUS_SELL: "&fa shop oriented booster which multiplies;&fshop sells by the given booster magnitude"
  SHOP_GUI_PLUS_DISCOUNT: "&fa shop oriented booster which refunds shop;&fpurchases by the given booster magnitude"
  CROP_GROWTH: "&fa crop growth oriented booster;&fwhich speeds up crop growth times"
  MOB_DROPS: "&fa drops oriented booster which;&fincreases drops gained by killed mobs"
  MCMMO_GAIN: "&fa mcmmo oriented booster which;&fincreases mcmmo experience gained"
  EXPERIENCE_GAIN: "&fa vanilla oriented booster which;&fincreases experience gained"
  SHOP_KEEPER_TRADE_DISCOUNT: "&fdecreases the amount of items required;&fto trade with villagers"

booster-voucher-preset:
  common:
    material: NAME_TAG
    display: "{formatted-type} Booster"
    lore:
      - " &7&l| &fMagnitude: &b{magnitude}x"
      - " &7&l| &fDuration: &b{duration}"
      - " &7&l| &fScope: &b{scope}"
      - ""
      - "&f%boosters_booster-description;{type}; &7&l| %"
      - ""
      - "&7&oClick to redeem this booster"
  rare:
    material: NAME_TAG
    display: "{formatted-type} Booster"
    lore:
      - " &7&l| &fMagnitude: &b{magnitude}x"
      - " &7&l| &fDuration: &b{duration}"
      - " &7&l| &fScope: &b{scope}"
      - ""
      - "&f%boosters_booster-description;{type}; &7&l| %"
      - ""
      - "&7&oClick to redeem this booster"
    enchantments:
      DURABILITY: 0
    flags:
      - "HIDE_ENCHANTMENTS"
```