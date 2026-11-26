# DeluxeShopNK

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-Bedrock%20Edition-43A047?style=for-the-badge&logo=minecraft&logoColor=white" alt="Minecraft Bedrock Edition">
  <img src="https://img.shields.io/badge/Server-NukkitPetteriM1Edition-2196F3?style=for-the-badge" alt="NukkitPetteriM1Edition">
  <img src="https://img.shields.io/badge/Java-8-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 8">
  <img src="https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/Economy-LlamaEconomy%20%7C%20EconomyAPI-2E8B57?style=for-the-badge" alt="Economy providers">
  <img src="https://img.shields.io/badge/License-MIT-8A2BE2?style=for-the-badge" alt="MIT License">
</p>

<p align="center"><b>Tienda configurable para servidores NukkitPetteriM1Edition, con categorías ilimitadas, compras por cantidad, múltiples economías e iconos del paquete de recursos en los botones de Bedrock Forms.</b></p>

---

**DeluxeShopNK** es un plugin de tienda para **NukkitPetteriM1Edition (PM1E)**, reconstruido y organizado con Maven a partir del antiguo LlamaShop. Permite organizar artículos por categorías, configurar precios y encantamientos, y utilizar la economía principal del servidor o monedas secundarias compatibles.

- **Autor:** BrunoJaegerK
- **Java:** 8
- **Build:** Maven
- **Servidor objetivo:** NukkitPetteriM1Edition (Minecraft Bedrock)

## ✨ Características

- Categorías configurables sin un límite fijo de cinco.
- Menú principal y navegación de regreso entre categorías.
- Compra de artículos por cantidad.
- Validación de cantidades y espacio disponible en el inventario.
- Validación del saldo y del resultado del pago antes de entregar los artículos.
- Compatibilidad con **LlamaEconomy** y **EconomyAPI**, con detección automática.
- Compatibilidad opcional con monedas secundarias de **EconomyExpandNK**.
- Artículos con encantamientos configurables, incluidos libros encantados.
- Iconos de artículos desde el paquete de recursos Bedrock mediante rutas como `textures/items/diamond`.
- Evento de compra para integraciones con otros plugins.

## 🧩 Dependencias

| Dependencia | Uso | Requerida |
|---|---|---|
| NukkitPetteriM1Edition | API del servidor | Sí |
| LlamaEconomy | Economía principal | Una economía principal |
| EconomyAPI | Economía principal | Una economía principal |
| EconomyExpandNK | Monedas secundarias por artículo | Solo si se usan esas monedas |

**Economía principal:** puedes utilizar LlamaEconomy o EconomyAPI. Con `economy.provider: auto`, el plugin intenta detectar primero LlamaEconomy y después EconomyAPI.

**EconomyExpandNK:** es opcional. Solo se necesita cuando configures artículos que utilicen identificadores de monedas secundarias proporcionadas por ese plugin.

## 📥 Instalación

1. Descarga el JAR de DeluxeShopNK desde la sección de [Actions](https://github.com/BrunoJaegerK/DeluxeShopNK/actions) o compílalo desde el código fuente.
2. Comprueba que tu servidor utiliza NukkitPetteriM1Edition.
3. Instala LlamaEconomy o EconomyAPI para habilitar las compras con la moneda principal.
4. Instala EconomyExpandNK únicamente si vas a utilizar sus monedas secundarias.
5. Copia `DeluxeShopNK.jar` en la carpeta `plugins/`.
6. Inicia o reinicia el servidor.
7. Edita `plugins/DeluxeShopNK/config.yml` para configurar categorías, artículos, precios y mensajes.

## 🎮 Comandos

El comando y sus alias se pueden personalizar desde la sección `Commands` de `config.yml`.

| Comando predeterminado | Descripción |
|---|---|
| `/shop` | Abre la tienda. |
| `/playershop` | Alias configurable del comando principal. |
| `/tienda` | Alias configurable del comando principal. |

## 🔐 Permisos

La configuración actual no declara un permiso propio para el comando de tienda. El acceso se gestiona mediante el registro del comando de Nukkit y las restricciones que establezcas en tu servidor.

## ⚙️ Configuración

Archivo: `plugins/DeluxeShopNK/config.yml`

### Categorías y artículos

Cada sección dentro de `ShopForm` representa una categoría. Puedes añadir, quitar o renombrar categorías desde el archivo de configuración.

Formato básico:

```yaml
ShopForm:
  Resources:
    - "Cobblestone:4:0:1.3"
    - "Coal:263:0:3"
  Materials:
    - "Oak Wood:17:0:5.3"
  Special:
    - "Beacon:138:0:10000"
```

### Formato de los artículos

Formato compatible con las entradas antiguas:

```yaml
- "Nombre:ID:Meta:Precio"
```

Formato con moneda y encantamientos:

```yaml
- "Nombre:ID:Meta:Precio:CurrencyID:EnchantID=Level,EnchantID=Level"
```

También puedes añadir la ruta de la textura después de `|`:

```yaml
- "Nombre:ID:Meta:Precio:CurrencyID:EnchantID=Level|textures/items/diamond_sword"
```

- **Nombre:** nombre que se muestra en la tienda.
- **ID:** identificador del artículo de Nukkit.
- **Meta:** daño o variante del artículo.
- **Precio:** coste por unidad.
- **CurrencyID:** identificador de moneda; utiliza `default` para la economía principal.
- **EnchantID=Level:** encantamiento y nivel. Se pueden añadir varios separados por comas.
- **Ruta de textura:** ruta dentro del paquete de recursos Bedrock, por ejemplo `textures/items/diamond`. No escribas la extensión `.png`.

Ejemplo:

```yaml
ShopForm:
  Resources:
    - "Coal:263:0:3|textures/items/coal"
    - "Iron:265:0:14.9|textures/items/iron_ingot"
    - "Diamond:264:0:142|textures/items/diamond"
  Enchanted:
    - "Sharpness V Diamond Sword:276:0:2500:default:9=5|textures/items/diamond_sword"
```

### 🖼️ Iconos mediante `textures/items`

DeluxeShopNK utiliza el mismo mecanismo de imágenes locales de Bedrock Forms que NeoBuyerV2.2: el botón se crea con `ElementButtonImageData.IMAGE_DATA_TYPE_PATH`. No descarga imágenes ni utiliza URLs externas.

- La ruta se resuelve desde el paquete de recursos que recibe el cliente Bedrock.
- Para artículos, utiliza rutas como `textures/items/iron_ingot`, `textures/items/diamond` o `textures/items/diamond_sword`.
- La ruta debe existir en el paquete de recursos activo del jugador. Si utilizas texturas personalizadas, asegúrate de que tu paquete las incluya y se distribuya a los jugadores.
- Normalmente se omite la extensión `.png`.
- No añadas URLs como `https://...`: el sistema anterior de imágenes por URL fue retirado.
- Si no se especifica una ruta, el botón se muestra sin imagen.

**Importante:** el plugin no incorpora ni distribuye un paquete de texturas. Solo referencia las rutas existentes en el paquete de recursos Bedrock del cliente.

Para monedas secundarias, reemplaza `default` por el identificador correspondiente registrado en EconomyExpandNK.

### Economía

```yaml
economy:
  # auto | llamaeconomy | economyapi | disabled
  provider: auto
```

| Valor | Comportamiento |
|---|---|
| `auto` | Detecta LlamaEconomy primero y después EconomyAPI. |
| `llamaeconomy` | Utiliza LlamaEconomy. |
| `economyapi` | Utiliza EconomyAPI. |
| `disabled` | Desactiva las operaciones económicas. |

Las monedas de EconomyExpandNK se seleccionan individualmente en cada entrada de artículo mediante `CurrencyID`.

### Interfaz y límites

En `config.yml` también puedes personalizar:

- `max-purchase-amount`: cantidad máxima permitida por compra.
- `ui.title`: título del menú principal.
- `ui.main-content`: texto del menú principal.
- `ui.category-content`: texto de las categorías.
- `ui.item-button`: formato del botón de cada artículo.
- `ui.back-button`: texto del botón para regresar.
- `ui.purchase-title`: título del formulario de compra.
- `ui.purchase-prompt`: texto que solicita la cantidad.
- `ui.amount-placeholder`: marcador del campo de cantidad.
- `messages`: mensajes de validación, pago y confirmación.

Puedes utilizar los marcadores `{item}`, `{price}` y `{currency}` en los textos correspondientes; el mensaje de compra también utiliza `{amount}`.

## 🔌 Evento de compra

Otros plugins pueden escuchar:

`com.brunojaeger.deluxeshop.events.ShopPurchaseEvent`

El evento se dispara después de una compra exitosa y expone información del jugador, el artículo, la cantidad, el total y el identificador de moneda. Puede utilizarse para crear estadísticas, misiones o integraciones propias.

## 🛠️ Compilación

### Requisitos

- JDK 8 o superior.
- Maven.
- JAR de NukkitPetteriM1Edition compatible con la versión objetivo.

### Dependencia local

Coloca el JAR del servidor en la siguiente ruta:

```text
DeluxeShopNK/
├── libs/
│   └── Nukkit-PM1E.jar
├── src/
├── pom.xml
└── README.md
```

### Generar el plugin

Desde la raíz del proyecto ejecuta:

```bash
mvn clean package
```

El archivo compilado se genera en:

```text
target/DeluxeShopNK.jar
```

GitHub Actions prepara la dependencia de PM1E y ejecuta la compilación automatizada.

## ℹ️ About

Tienda configurable para NukkitPetteriM1Edition, con categorías personalizables, múltiples economías, encantamientos e iconos de artículos desde el paquete de recursos Bedrock.

- **Autor:** BrunoJaegerK
- **Servidor objetivo:** NukkitPetteriM1Edition (Minecraft Bedrock)
- **Repositorio:** https://github.com/BrunoJaegerK/DeluxeShopNK

## 📜 Licencia

Este proyecto se distribuye bajo la licencia **MIT**. Puedes utilizarlo, modificarlo y redistribuirlo respetando sus condiciones y conservando los avisos de copyright y licencia.

Consulta el archivo [LICENSE](LICENSE) para leer el texto completo.

---

<p align="center">
  <b>DeluxeShopNK</b> · Una tienda flexible para tu servidor Bedrock.
</p>
