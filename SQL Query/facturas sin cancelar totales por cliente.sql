--TOTALES FACTURAS SIN CANCELAR TOTALES POR CLIENTE
SELECT
CLIENTE,
SUM(EXENTO) AS EXENTO,
SUM(GRAVADO) AS GRAVADO,
SUM(IMPUESTO) AS IMPUESTO,
SUM(TOTAL) AS TOTAL
FROM
FACTURACION_MANUAL
WHERE
TIPO IS NOT NULL
AND FECHACANCELACION IS NULL
GROUP BY CLIENTE