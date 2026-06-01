const searchInput = document.getElementById("searchInput");

if (searchInput) {
    searchInput.addEventListener("keyup", function () {

        let filtro = searchInput.value.toLowerCase();
        let tableId = searchInput.dataset.tableTarget || "tablaUsuarios";
        let filas = document.querySelectorAll(`#${tableId} tbody tr`);

        filas.forEach(fila => {

            let texto = fila.textContent.toLowerCase();

            fila.style.display = texto.includes(filtro)
                ? ""
                : "none";

        });

    });
}

const ventaForm = document.getElementById("ventaForm");

if (ventaForm) {
    const fechaHoraVenta = document.getElementById("fechaHoraVenta");
    const productoSelect = document.getElementById("productoSelect");
    const cantidadProducto = document.getElementById("cantidadProducto");
    const agregarProductoVenta = document.getElementById("agregarProductoVenta");
    const tablaDetalleVenta = document.querySelector("#tablaDetalleVenta tbody");
    const totalVentaTexto = document.getElementById("totalVentaTexto");

    const formatoMoneda = new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP"
    });

    function actualizarFechaHora() {
        const ahora = new Date();
        fechaHoraVenta.value = ahora.toLocaleString("es-CO", {
            dateStyle: "short",
            timeStyle: "medium"
        });
    }

    function recalcularVenta() {
        let total = 0;

        tablaDetalleVenta.querySelectorAll("tr").forEach(fila => {
            const precio = Number(fila.dataset.precio);
            const cantidadInput = fila.querySelector(".cantidad-detalle");
            const cantidad = Number(cantidadInput.value);
            const stock = Number(fila.dataset.stock);
            const subtotal = precio * cantidad;

            if (cantidad > stock) {
                cantidadInput.value = stock;
            }

            const cantidadActual = Number(cantidadInput.value);
            const subtotalActual = precio * cantidadActual;

            fila.querySelector(".subtotal-detalle").textContent = formatoMoneda.format(subtotalActual);
            total += subtotalActual;
        });

        totalVentaTexto.textContent = formatoMoneda.format(total);
    }

    function crearInputOculto(nombre, valor) {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = nombre;
        input.value = valor;
        return input;
    }

    function agregarProducto() {
        const opcion = productoSelect.options[productoSelect.selectedIndex];

        if (!opcion || !opcion.value) {
            alert("Seleccione un producto");
            return;
        }

        const productoId = opcion.value;
        const nombre = opcion.dataset.nombre;
        const precio = Number(opcion.dataset.precio);
        const stock = Number(opcion.dataset.stock);
        const cantidad = Number(cantidadProducto.value);

        if (!cantidad || cantidad <= 0) {
            alert("Ingrese una cantidad mayor a cero");
            return;
        }

        if (cantidad > stock) {
            alert("La cantidad no puede superar el stock disponible");
            return;
        }

        const filaExistente = tablaDetalleVenta.querySelector(`tr[data-producto-id="${productoId}"]`);

        if (filaExistente) {
            const cantidadExistente = filaExistente.querySelector(".cantidad-detalle");
            const nuevaCantidad = Number(cantidadExistente.value) + cantidad;

            if (nuevaCantidad > stock) {
                alert("La cantidad acumulada supera el stock disponible");
                return;
            }

            cantidadExistente.value = nuevaCantidad;
            filaExistente.querySelector('input[name="cantidades"]').value = nuevaCantidad;
            recalcularVenta();
            return;
        }

        const fila = document.createElement("tr");
        fila.dataset.productoId = productoId;
        fila.dataset.precio = precio;
        fila.dataset.stock = stock;

        fila.innerHTML = `
            <td>
                <strong>${nombre}</strong>
                <small class="block text-gray-500">Stock disponible: ${stock}</small>
            </td>
            <td>${formatoMoneda.format(precio)}</td>
            <td>
                <input type="number"
                       min="1"
                       max="${stock}"
                       step="1"
                       value="${cantidad}"
                       class="cantidad-detalle w-24 rounded-xl border border-gray-200 bg-gray-50 px-3 py-2 outline-none">
            </td>
            <td class="subtotal-detalle font-bold text-[#a11692]"></td>
            <td>
                <button type="button" class="btn-action btn-delete eliminar-detalle">
                    🗑️
                </button>
            </td>
        `;

        const celdaProducto = fila.querySelector("td");
        celdaProducto.appendChild(crearInputOculto("productoIds", productoId));
        celdaProducto.appendChild(crearInputOculto("cantidades", cantidad));
        tablaDetalleVenta.appendChild(fila);
        recalcularVenta();
    }

    actualizarFechaHora();
    setInterval(actualizarFechaHora, 1000);

    agregarProductoVenta.addEventListener("click", agregarProducto);

    tablaDetalleVenta.addEventListener("input", function (event) {
        if (event.target.classList.contains("cantidad-detalle")) {
            const fila = event.target.closest("tr");
            const stock = Number(fila.dataset.stock);
            let cantidad = Number(event.target.value);

            if (!cantidad || cantidad < 1) {
                cantidad = 1;
            }

            if (cantidad > stock) {
                cantidad = stock;
            }

            event.target.value = cantidad;
            fila.querySelector('input[name="cantidades"]').value = cantidad;
            recalcularVenta();
        }
    });

    tablaDetalleVenta.addEventListener("click", function (event) {
        if (event.target.classList.contains("eliminar-detalle")) {
            event.target.closest("tr").remove();
            recalcularVenta();
        }
    });

    ventaForm.addEventListener("submit", function (event) {
        if (!tablaDetalleVenta.querySelector("tr")) {
            event.preventDefault();
            alert("Debe agregar al menos un producto a la venta");
        }
    });
}
