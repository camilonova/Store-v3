--FACTURAS CANCELADAS FILTRADAS POR FECHA
SELECT
CLIENTE,
SUM(EXENTO) AS EXENTO,
SUM(GRAVADO) AS GRAVADO,
SUM(IMPUESTO) AS IMPUESTO,
SUM(TOTAL) AS TOTAL
FROM
FACTURACION_MANUAL
WHERE
FECHACANCELACION IS NOT NULL
AND FECHA
BETWEEN
'2005-01-01' AND '2007-01-01'
GROUP BY
CLIENTE