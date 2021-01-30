# Boosters
Introduces Boosters for plugins such as ShopGUIPlus, GriefPrevention, McMMO
<br>
Version: `1.0.5-Release` <br>
Native API: `Paper-1.16.4-R0.1-SNAPSHOT` <br>
Source Code: <a href="https://github.com/Splice-Games/Boosters">github.com/Splice-Games/Boosters</a> <br>
Developer: `Frcsty` <br>

## How To (Server Owner)
This is a plugin built on PaperAPI, and is required to properly run this plugin.

<b>Installation:</b> <br>
| Place SGBoosters-VERSION.jar (`SGBoosters-1.0.5-Release.jar`) file into the plugins folder. <br>
| Start the server, plugin will generate `SGBoosters` directory with files:
* `config.yml`
* `menu/booster-list-menu.yml` <br>
* `booster-storage.yml` (temporary file for persistency) <br>

| Stop the server after everything has been loaded. <br>
| Open and configure the plugin to your needs. <br>
| Start the server and enjoy the plugin!

## Commands
The plugin provides two commands to operate it. <br>

- `/booster` <br>
  Grants a booster voucher of a specific type to the targeted player <br>
  ie. `/booster give Frcsty SELL ALL 1.035 3600` <br>
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
The plugin as of right now provides `5` different Booster types, however is designed in a way <br> 
that more can easily be added in the future. <br>
Display format: `BoosterType (Required Plugin)`

- `SELL` (ShopGUIPlus) <br>
    *Description:* Is triggered when a user successfully sells items to the shop, <br>
    multiplies the gained amount of money with the given booster magnitude.


- `DISCOUNT` (ShopGUIPlus) <br>
    *Description:* Is triggered when a user successfully purchases items from the shop, <br>
    refunds a percentage of the purchase cost depending on the booster magnitude.
  

- `MCMMO_GAIN` (McMMO) <br>
    *Description:* Multiplies the gained experience when receiving McMMO experience, <br>
    by the given booster magnitude.


- `CROP_GROWTH` (GriefPrevention) <br>
    *Description:* Speeds up crop growth within player claims by the given booster magnitude, <br>
    from the amount of active boosters: `1 Booster -> 2x, 2 Boosters -> 3x`
  

- `MOB_DROPS` (GriefPrevention) <br>
    *Description:* Increases the mob drops gained from mobs within claims depending on the <br>
    given booster magnitude: `< 1.15 -> +1, > 1.5 -> +2` 