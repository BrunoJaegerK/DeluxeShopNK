# DeluxeShopNK

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-Bedrock%20Edition-43A047?style=for-the-badge&logo=minecraft&logoColor=white" alt="Minecraft Bedrock Edition">
  <img src="https://img.shields.io/badge/Server-NukkitPetteriM1Edition-2196F3?style=for-the-badge" alt="NukkitPetteriM1Edition">
  <img src="https://img.shields.io/badge/Java-8-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 8">
  <img src="https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/Economy-LlamaEconomy%20%7C%20EconomyAPI-2E8B57?style=for-the-badge" alt="Economy providers">
  <img src="https://img.shields.io/badge/License-MIT- blue?style=for-the-badge" alt="MIT License">
</p>

<p align="center"><b>Tienda configurable para servidores NukkitPetteriM1Edition, con categorías ilimitadas, compras por cantidad, múltiples economías e imágenes PNG en los botones de Bedrock Forms.</b></p>

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
- Imágenes PNG mediante URL pública en los botones de artículos de Bedrock Forms.
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

- **Nombre:** nombre que se muestra en la tienda.
- **ID:** identificador del artículo de Nukkit.
- **Meta:** daño o variante del artículo.
- **Precio:** coste por unidad.
- **CurrencyID:** identificador de moneda; utiliza `default` para la economía principal.
- **EnchantID=Level:** encantamiento y nivel. Se pueden añadir varios separados por comas.

Ejemplo:

```yaml
ShopForm:
  Enchanted:
    - "Sharpness V Diamond Sword:276:0:2500:default:9=5"
    - "Efficiency V Diamond Pickaxe:278:0:3500:default:15=5"
    - "Enchanted Book Sharpness V:403:0:1200:default:9=5"
```

Para monedas secundarias, reemplaza `default` por el identificador correspondiente registrado en EconomyExpandNK.

### 🖼️ Imágenes PNG en Bedrock Forms

Puedes añadir una imagen a un botón de artículo colocando el carácter `|` después de la entrada y una URL pública directa a la imagen.

```yaml
ShopForm:
  Resources:
    - "Cobblestone:4:0:1.3|https://example.com/images/cobblestone.png"
    - "Diamond:264:0:142|https://example.com/images/diamond.png"
```

- Utiliza enlaces directos a archivos de imagen PNG.
- Se recomienda utilizar HTTPS.
- El enlace debe ser accesible públicamente y permitir que los clientes Bedrock descarguen la imagen.
- La imagen es opcional; las entradas sin URL conservan sus botones normales.
- La imagen se muestra en el botón del artículo dentro del menú de categoría.

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

Tienda configurable para NukkitPetteriM1Edition, con categorías personalizables, múltiples economías, encantamientos e imágenes PNG en los botones de Bedrock Forms.

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
