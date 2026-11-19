# DeluxeShopNK

Shop plugin for Nukkit PetteriM1 Edition, reconstructed from the original LlamaShop 1.0.0-SNAPSHOT with a modern Maven layout.

## Features
- Configurable categories with no hard-coded five-category cap.
- Back navigation from category menus.
- Quantity purchases, inventory-space validation and payment validation.
- Primary economy compatibility: **LlamaEconomy** and **EconomyAPI** (auto-detection).
- Optional secondary currencies from **EconomyExpandNK**.
- Configurable enchantments on items and enchanted books.
- Purchase event for integrations.

## Requirements
- Nukkit PetteriM1 Edition (PM1E).
- LlamaEconomy or EconomyAPI for items using `default` currency.
- EconomyExpandNK is optional and required only for entries using secondary currency IDs.

## Install
Place the compiled `target/DeluxeShopNK.jar` into `plugins/`, restart the server, then edit `plugins/DeluxeShopNK/config.yml`.

## Shop item format
Legacy format is preserved:

```yaml
- "Display Name:ItemID:Meta:Price"
```

Optional currency and enchantments:

```yaml
- "Display Name:ItemID:Meta:Price:CurrencyID:EnchantID=Level,EnchantID=Level"
```

Use `default` for the primary economy, or a currency ID defined in EconomyExpandNK. Enchantment IDs follow the Nukkit item enchantment registry. Example:

```yaml
ShopForm:
  Blocks:
    - "Stone:1:0:2.5"
  Special:
    - "Crystal Key:131:0:15:crystals"
  Enchanted:
    - "Sharpness V Sword:276:0:1200:default:9=5"
    - "Sharpness V Book:403:0:600:crystals:9=5"
```

Categories can be added freely under `ShopForm`.

### PNG button images (Bedrock Forms)

Append a pipe (`|`) and a publicly accessible image URL to any item entry. The URL is shown as a Bedrock button image in the category item list. Use direct PNG image links (HTTPS recommended); images are optional.

```yaml
ShopForm:
  Blocks:
    - "Stone:1:0:2.5|https://your-host.com/images/stone.png"
```

The image host must allow Bedrock clients to fetch the image directly. URLs with unsupported schemes are rejected.


## Economy settings
```yaml
economy:
  provider: auto
```
Values: `auto`, `llamaeconomy`, `economyapi`, or `disabled`. Auto prefers LlamaEconomy then EconomyAPI. EconomyExpandNK currencies are selected per item.

## Purchase event
`com.brunojaeger.deluxeshop.events.ShopPurchaseEvent` fires after a successful purchase and exposes the player, item details, amount, total and currency ID.

## Build
Java 17 and Maven are required. Put the official PM1E server jar at `libs/Nukkit-PM1E.jar` before running Maven. GitHub Actions downloads this jar automatically.

```bash
mkdir -p libs
curl -fL --retry 3 -o libs/Nukkit-PM1E.jar https://github.com/PetteriM1/NukkitPetteriM1Edition/releases/latest/download/Nukkit-PM1E.jar
mvn clean package
```

Output: `target/DeluxeShopNK.jar`.

## License
MIT.