package com.example.data.legal

data class CrimeTypology(
    val id: String,
    val name: String,
    val shortName: String,
    val legalConcept: String,
    val criminalElements: List<String>,
    val penaltyRange: String,
    val practicalExample: String,
    val evidenceChecklist: List<String>
)

data class EvidenceProtocol(
    val title: String,
    val category: String,
    val keyRule: String,
    val detailedSteps: List<String>,
    val commonMistakesToAvoid: List<String>
)

object LegalKnowledgeBase {

    val CRIME_TYPOLOGIES: List<CrimeTypology> = listOf(
        CrimeTypology(
            id = "cohecho_soborno",
            name = "Cohecho y Soborno (Pasivo y Activo)",
            shortName = "Cohecho / Soborno",
            legalConcept = "El cohecho se configura cuando un funcionario público solicita, acepta o recibe donativo, promesa o cualquier ventaja para realizar u omitir un acto propio de su función (Cohecho Pasivo), o cuando un particular entrega, promete u ofrece dicha dádiva para obtener una ventaja ilegítima (Cohecho Activo).",
            criminalElements = listOf(
                "Sujeto activo especial calificado (funcionario o servidor público en cohecho pasivo) o común (particular en cohecho activo).",
                "Verbo rector: solicitar, aceptar, recibir, donar, prometer u ofrecer.",
                "Objeto material: donativo, dinero, prebenda, dádiva o ventaja patrimonial/extrapatrimonial.",
                "Nexo causal: la dádiva condiciona la infracción de deberes o la ejecución de un acto funcionarial."
            ),
            penaltyRange = "Privación de libertad de 4 a 10 años (hasta 15 años en cohecho pasivo específico de jueces o fiscales), más inhabilitación para ejercer cargos públicos y multas.",
            practicalExample = "Un inspector municipal que solicita $2,000 USD a un comerciante para no clausurar un local, o un proveedor que entrega un soborno al comité de compras para adjudicarse un contrato.",
            evidenceChecklist = listOf(
                "Capturas de pantalla completas de chats donde se solicita o pacta la dádiva.",
                "Grabación de audio de la conversación donde el funcionario exige o acepta la ventaja.",
                "Comprobantes de transferencias bancarias, depósitos a testaferros o cuentas puente.",
                "Registros de visitas o bitácoras de acceso al despacho público."
            )
        ),
        CrimeTypology(
            id = "peculado_malversacion",
            name = "Peculado y Malversación de Fondos Públicos",
            shortName = "Peculado / Malversación",
            legalConcept = "El peculado sanciona al funcionario que se apropia o utiliza para beneficio propio o de terceros caudales o bienes del Estado cuya administración o custodia le fue confiada. La malversación castiga el desvío de partidas presupuestales a un destino público diferente afectando el servicio o programa original.",
            criminalElements = listOf(
                "Relación funcional: el funcionario debe tener la administración, percepción o custodia de los bienes por razón de su cargo.",
                "Peculado por apropiación: incorporación definitiva de los caudales públicos al patrimonio personal o de un tercero.",
                "Peculado de uso: empleo indebido de vehículos, maquinaria o personal del Estado para fines particulares.",
                "Malversación: afectación presupuestal indebida sin apropiación personal, pero vulnerando la ley de presupuesto."
            ),
            penaltyRange = "Peculado: 4 a 12 años de prisión según el monto (agravado si supera umbrales o involucra programas sociales). Malversación: 1 a 4 años e inhabilitación.",
            practicalExample = "El tesorero que transfiere dinero del fondo rotatorio a su cuenta personal, o el alcalde que utiliza maquinaria pesada del municipio para nivelar el terreno de su finca privada.",
            evidenceChecklist = listOf(
                "Informes contables, arqueos de caja y conciliaciones bancarias oficiales.",
                "Órdenes de pago con cheques o transferencias sin sustento documentario.",
                "Fotografías o videos con fecha geolocalizada de bienes públicos en propiedades privadas.",
                "Registros de bitácora y combustible de vehículos oficiales."
            )
        ),
        CrimeTypology(
            id = "colusion_licitaciones",
            name = "Colusión Ilegal en Licitaciones y Contrataciones",
            shortName = "Colusión en Licitaciones",
            legalConcept = "Consiste en la concertación clandestina e ilícita entre funcionarios públicos pertenecientes a comités de selección y postores interesados en contratos, suministros, consultorías o licitaciones públicas, con el fin de defraudar al Estado.",
            criminalElements = listOf(
                "Concertación clandestina: acuerdo colusorio bajo la mesa entre funcionarios y postores.",
                "Colusión Simple: basta el pacto concertado para defraudar (delito de peligro).",
                "Colusión Agravada: cuando se produce perjuicio patrimonial efectivo al erario (sobrecostos, obras fantasmas).",
                "Mecanismos típicos: fraccionamiento ilícito de contratos, direccionamiento de especificaciones técnicas (TDR a la medida), cotizaciones de favor."
            ),
            penaltyRange = "Colusión Simple: 3 a 6 años de prisión. Colusión Agravada: 6 a 15 años de prisión efectiva más inhabilitación perpetua.",
            practicalExample = "Un comité de licitación que elabora términos de referencia con marcas exclusivas para favorecer a una empresa predeterminada, o tres postores que presentan ofertas coordinadas con precios inflados para repartirse los lotes.",
            evidenceChecklist = listOf(
                "Bases de la licitación y cartas de invitación comparadas con el catálogo del postor ganador.",
                "Expediente técnico con modificaciones súbitas de especificaciones.",
                "Cotizaciones gemelas que comparten metadatos informáticos o el mismo creador de archivo PDF.",
                "Relaciones societarias cruzadas o vínculos familiares entre postores rivales."
            )
        ),
        CrimeTypology(
            id = "nepotismo_incompatibilidades",
            name = "Nepotismo e Incompatibilidades Funcionales",
            shortName = "Nepotismo",
            legalConcept = "Prohibición legal que sanciona a funcionarios con facultad de nombramiento o contratación que favorecen o contratan a sus parientes (generalmente hasta el cuarto grado de consanguinidad y segundo de afinidad, o cónyuge/conviviente) en el sector público.",
            criminalElements = listOf(
                "Facultad de nombramiento o contratación directa o injerencia directa en la selección.",
                "Vínculo de parentesco directo (padres, hijos, hermanos, tíos, sobrinos, suegros, cuñados).",
                "Interés indebido en provecho propio o de parientes en negociaciones del Estado."
            ),
            penaltyRange = "Destitución del cargo, inhabilitación administrativa para la función pública de 1 a 5 años y responsabilidad penal por negociación incompatible (4 a 6 años).",
            practicalExample = "Un director regional que contrata como jefa de compras a su cuñada o a los hijos de su cónyuge simulando un proceso de selección abierto.",
            evidenceChecklist = listOf(
                "Partidas de nacimiento o actas de matrimonio que acrediten el árbol genealógico.",
                "Contratos de servicios u órdenes de locación de servicios suscritas.",
                "Resolución de designación suscrita por el funcionario pariente o por un subordinado directo."
            )
        ),
        CrimeTypology(
            id = "trafico_influencias",
            name = "Tráfico de Influencias y Patrocinio Ilegal",
            shortName = "Tráfico de Influencias",
            legalConcept = "Ocurre cuando una persona, invocando o teniendo influencias reales o simuladas, recibe, hace dar o prometer para sí o para un tercero un donativo, promesa o ventaja, con el ofrecimiento de interceder ante un funcionario público que esté conociendo o haya conocido un caso judicial o administrativo.",
            criminalElements = listOf(
                "Invocación de influencias reales o simuladas (el intermediario afirma tener 'llegada' al decisor).",
                "Ofrecimiento de intercesión ante funcionario que conoce causa judicial o expediente público.",
                "Solicitud o recepción de beneficio o promesa de compensación."
            ),
            penaltyRange = "4 a 8 años de privación de libertad (agravado si quien trafica influencias es funcionario público).",
            practicalExample = "Un asesor que cobra una suma de dinero asegurando al interesado que convencerá al fiscal o al director de licencias para que archive su expediente o le conceda el permiso.",
            evidenceChecklist = listOf(
                "Mensajes de audio o texto donde el intermediario afirma su capacidad de 'arreglar' el fallo.",
                "Comprobantes de entrega de dinero en efectivo o transferencias.",
                "Registros de llamadas telefónicas entre el traficante y el funcionario que decide la causa."
            )
        ),
        CrimeTypology(
            id = "enriquecimiento_ilicito",
            name = "Enriquecimiento Ilícito",
            shortName = "Enriquecimiento Ilícito",
            legalConcept = "Se produce cuando un funcionario público incrementa ilícitamente su patrimonio o gasto económico personal de manera desproporcionada en comparación con sus ingresos legítimos durante el ejercicio de sus funciones, sin poder justificar su procedencia.",
            criminalElements = listOf(
                "Sujeto calificado: funcionario público en ejercicio.",
                "Desbalance patrimonial no justificado contablemente.",
                "Adquisición de bienes de alto valor a través de testaferros o sociedades pantalla."
            ),
            penaltyRange = "5 a 10 años de prisión (hasta 15 años en altos funcionarios del Estado) más decomiso de bienes.",
            practicalExample = "Un funcionario con sueldo mensual de $1,500 USD que en dos años adquiere inmuebles de lujo y vehículos de alta gama a nombre de terceros sin justificación de préstamos o herencias.",
            evidenceChecklist = listOf(
                "Declaraciones juradas de bienes y rentas oficiales comparadas con registros públicos de propiedad.",
                "Movimientos migratorios con viajes costosos no acordes al salario.",
                "Partidas registrales de compras inmobiliarias o vehiculares recientes."
            )
        )
    )

    val EVIDENCE_PROTOCOLS: List<EvidenceProtocol> = listOf(
        EvidenceProtocol(
            title = "Cadena de Custodia Digital y Huella Hash",
            category = "Evidencia Digital",
            keyRule = "Nunca edites ni renombres el archivo original. Conserva siempre el archivo nativo y genera un duplicado forense con hash SHA-256.",
            detailedSteps = listOf(
                "1. Preservación del original: Guarda el archivo (video, audio, foto o documento) en un almacenamiento protegido contra escritura.",
                "2. Cálculo de Hash SHA-256: Esta huella digital matemática garantiza ante el juez que el archivo no fue alterado ni un solo byte desde su obtención.",
                "3. Metadatos EXIF / Cabeceras: No tomes capturas recortadas de las fotos; el archivo original contiene la fecha, hora exacta, coordenadas GPS y modelo del dispositivo.",
                "4. Registro de custodia: Anota fecha, hora, dispositivo donde se grabó o descargó y quién tuvo acceso a él."
            ),
            commonMistakesToAvoid = listOf(
                "Reenviar el audio o video por WhatsApp básico (comprime y elimina metadatos forenses originales).",
                "Abrir y guardar archivos de Word o Excel modificando su fecha de última modificación.",
                "Recortar capturas de pantalla eliminando la barra de estado con la hora y fecha del sistema."
            )
        ),
        EvidenceProtocol(
            title = "Grabaciones de Audio y Video por Interlocutor",
            category = "Audios y Videos",
            keyRule = "En la doctrina procesal penal moderna, grabar una conversación en la que TÚ eres uno de los interlocutores activos NO es delito ni vulnera el secreto de las comunicaciones.",
            detailedSteps = listOf(
                "1. Participación activa: Asegúrate de que tu voz o presencia forme parte de la conversación grabada para legitimar la prueba.",
                "2. Contextualización previa: Antes de iniciar la conversación o al principio, menciona de forma natural la fecha, hora y lugar aproximado.",
                "3. Provocación prohibida: No induzcas ni presiones al funcionario a cometer el delito si no existía voluntad previa (evitar alegato de delito provocado).",
                "4. Conservación del soporte físico: Guarda el teléfono o grabadora con la que realizaste la captura, ya que la defensa podría solicitar peritaje fonético."
            ),
            commonMistakesToAvoid = listOf(
                "Colocar micrófonos ocultos en oficinas donde tú NO estás presente (eso constituye interceptación ilegal o espionaje).",
                "Filtrar el audio a redes sociales antes de formalizar la denuncia (pone en riesgo tu integridad y alerta al funcionario)."
            )
        ),
        EvidenceProtocol(
            title = "Chats de WhatsApp, Signal y Correos Electrónicos",
            category = "Mensajería y Correo",
            keyRule = "Exporta el chat completo con medios incluidos (.zip o .txt) y para correos extrae el código fuente o cabeceras completas (.eml o .msg).",
            detailedSteps = listOf(
                "1. Exportación íntegra: En WhatsApp usa 'Exportar chat con archivos multimedia' para obtener el archivo de texto secuencial completo.",
                "2. Capturas de pantalla continuas: Toma capturas que muestren el número de teléfono con código de país (no solo el alias guardado) y foto de perfil.",
                "3. Cabeceras completas de correo: Descarga el archivo de correo nativo (.eml). En Gmail o Outlook usa 'Ver código fuente del mensaje' para capturar la IP de origen, servidores SMTP y firmas DKIM.",
                "4. Copia en la nube cifrada: Sube la copia a un almacenamiento seguro bajo doble factor de autenticación."
            ),
            commonMistakesToAvoid = listOf(
                "Borrar partes de la conversación para 'resumir'; la defensa argumentará sesgo o descontextualización.",
                "Reenviar los mensajes individuales en lugar de exportar el historial continuo del hilo."
            )
        ),
        EvidenceProtocol(
            title = "Documentos Contractuales, Contables y Notariados",
            category = "Documentos Oficiales",
            keyRule = "Prioriza copias autenticadas, números de expediente digitalizados y actas de constatación notarial.",
            detailedSteps = listOf(
                "1. Copia idéntica: Escanea a color y a 300 DPI o más los contratos, adendas, órdenes de compra y facturas.",
                "2. Número de expediente y foliado: Registra el número correlativo de expediente (SIAF, SEACE, ComprANET o equivalente según tu país).",
                "3. Constatación Notarial: Si la prueba está en un sitio web público o portal de transparencia susceptible de ser borrado, solicita a un notario público un acta de constatación de visualización web.",
                "4. Testigos claves: Identifica nombres de funcionarios que visaron o tuvieron acceso al expediente."
            ),
            commonMistakesToAvoid = listOf(
                "Sustraer documentos clasificados de seguridad nacional sin asesoría legal especializada.",
                "Hacer marcas con resaltador o anotaciones a mano sobre el documento original."
            )
        )
    )

    fun getDirectOrientation(query: String): String {
        val lower = query.lowercase().trim()

        // Match based on keywords
        return when {
            lower.contains("soborno") || lower.contains("cohecho") || lower.contains("dadiva") || lower.contains("coima") -> {
                val c = CRIME_TYPOLOGIES.first { it.id == "cohecho_soborno" }
                """
                ⚖️ ORIENTACIÓN TÉCNICA: ${c.name}
                
                📌 Tipificación Penal:
                ${c.legalConcept}
                
                🔍 Elementos Esenciales del Delito:
                ${c.criminalElements.joinToString("\n") { "• $it" }}
                
                ⏳ Rango de Penas:
                ${c.penaltyRange}
                
                📁 Evidencias Clave para Sustentar la Denuncia:
                ${c.evidenceChecklist.joinToString("\n") { "✓ $it" }}
                
                💡 Protocolo de Seguridad: No borres ningún mensaje y no transfieras dinero para 'comprobar' el hecho sin coordinación fiscal.
                """.trimIndent()
            }
            lower.contains("peculado") || lower.contains("malversacion") || lower.contains("fondos publicos") || lower.contains("robar dinero") -> {
                val c = CRIME_TYPOLOGIES.first { it.id == "peculado_malversacion" }
                """
                ⚖️ ORIENTACIÓN TÉCNICA: ${c.name}
                
                📌 Diferencia Técnica Clave:
                • Peculado: El funcionario se apropia de los bienes/caudales o los usa para fines privados.
                • Malversación: Los fondos siguen siendo públicos pero se desvían ilegalmente a otra partida presupuestal sin autorización legal.
                
                🔍 Elementos de Tipicidad:
                ${c.criminalElements.joinToString("\n") { "• $it" }}
                
                ⏳ Sanciones Penales:
                ${c.penaltyRange}
                
                📁 Documentación Indispensable:
                ${c.evidenceChecklist.joinToString("\n") { "✓ $it" }}
                """.trimIndent()
            }
            lower.contains("colusion") || lower.contains("licitacion") || lower.contains("contratacion") || lower.contains("licitaciones") || lower.contains("tdr") || lower.contains("concurso") -> {
                val c = CRIME_TYPOLOGIES.first { it.id == "colusion_licitaciones" }
                """
                ⚖️ ORIENTACIÓN TÉCNICA: ${c.name}
                
                📌 Tipificación Penal:
                ${c.legalConcept}
                
                🔍 Tipos de Colusión:
                • Simple: Cuando los funcionarios conciertan con los postores para defraudar (se castiga el acuerdo ilícito).
                • Agravada: Cuando dicha concertación causa un perjuicio económico efectivo al Estado (sobrecostos, servicios no prestados).
                
                ⏳ Rango de Sanción:
                ${c.penaltyRange}
                
                📁 Pruebas Documentarias Clave:
                ${c.evidenceChecklist.joinToString("\n") { "✓ $it" }}
                """.trimIndent()
            }
            lower.contains("nepotismo") || lower.contains("familiar") || lower.contains("pariente") || lower.contains("cuñado") -> {
                val c = CRIME_TYPOLOGIES.first { it.id == "nepotismo_incompatibilidades" }
                """
                ⚖️ ORIENTACIÓN TÉCNICA: ${c.name}
                
                📌 Alcance Jurídico:
                ${c.legalConcept}
                
                🔍 Grados de Parentesco Prohibidos:
                • Hasta el 4° grado de consanguinidad (padres, hijos, abuelos, nietos, hermanos, tíos, sobrinos, primos hermanos).
                • Hasta el 2° grado de afinidad (cónyuge/conviviente, suegros, cuñados, yernos, nueras).
                
                ⏳ Consecuencias Legales:
                ${c.penaltyRange}
                
                📁 Evidencia Idónea:
                ${c.evidenceChecklist.joinToString("\n") { "✓ $it" }}
                """.trimIndent()
            }
            lower.contains("influencia") || lower.contains("trafico de influencias") || lower.contains("intermediario") || lower.contains("favores") -> {
                val c = CRIME_TYPOLOGIES.first { it.id == "trafico_influencias" }
                """
                ⚖️ ORIENTACIÓN TÉCNICA: ${c.name}
                
                📌 Tipificación:
                ${c.legalConcept}
                
                🔍 Aspecto Clave:
                El delito se consuma aunque la influencia sea falsa o simulada. Basta con que el sujeto afirme tener contactos y exija o reciba una dádiva para interceder.
                
                ⏳ Sanción:
                ${c.penaltyRange}
                
                📁 Elementos Probatorios:
                ${c.evidenceChecklist.joinToString("\n") { "✓ $it" }}
                """.trimIndent()
            }
            lower.contains("evidencia") || lower.contains("prueba") || lower.contains("grabar") || lower.contains("audio") || lower.contains("cadena de custodia") || lower.contains("whatsapp") -> {
                """
                🛡️ PROTOCOLO DE PRESERVACIÓN DE EVIDENCIAS LEGALES:
                
                1. 🎙️ Grabaciones de Audio/Video:
                   • Grabar conversaciones donde TÚ eres interlocutor es totalmente legal y admisible como prueba.
                   • No cortes ni edites el audio. Conserva el archivo nativo en el dispositivo original.
                   
                2. 💬 Mensajes y Chats (WhatsApp/Signal):
                   • No te limites a capturas aisladas. Exporta el chat completo (.txt con multimedia).
                   • Asegúrate de que el número telefónico con código internacional (+51, +57, +52, etc.) sea visible.
                   
                3. 🔐 Cadena de Custodia Digital:
                   • Genera un duplicado y calcula su código HASH (SHA-256). Esto certifica que la evidencia no ha sufrido alteraciones.
                   
                4. 📑 Documentos Oficiales:
                   • Identifica el número de expediente administrativo y solicita copias fedateadas o actas notariales de páginas web antes de que las eliminen.
                """.trimIndent()
            }
            lower.contains("anonimo") || lower.contains("denuncia") || lower.contains("seguridad") || lower.contains("proteccion") -> {
                """
                🔒 GARANTÍA DE ANONIMATO DAYANARA:
                
                En nuestro sistema:
                1. No registramos nombre, DNI, número telefónico ni correo.
                2. No se almacenan direcciones IP ni rastros de cookies.
                3. Tu reporte genera un Código Criptográfico Único (ej: DEN-XXXX) con hash SHA-256 para tu seguimiento privado.
                4. Toda la información queda almacenada de forma local y cifrada en tu dispositivo hasta que decidas remitirla formalmente.
                
                Para radicar una denuncia anónima formal, presiona el botón 'Nueva Denuncia' en la barra inferior.
                """.trimIndent()
            }
            else -> {
                """
                Hola, soy Dayanara, tu asesora legal especializada en delitos contra la administración pública y preservación probatoria.
                
                Puedo orientarte con precisión técnica sobre:
                • Tipificación penal de Sobornos y Cohecho (activo, pasivo y transnacional).
                • Peculado doloso, culposo y Malversación de fondos públicos.
                • Colusión ilegal en contrataciones y licitaciones públicas.
                • Nepotismo e incompatibilidades de funcionarios.
                • Tráfico de influencias y patrocinio ilícito.
                • Protocolos seguros para blindar evidencias digitales y cadena de custodia.
                
                ¿Qué consulta jurídica o caso sospechoso deseas que analicemos hoy? Puedes pulsar el micrófono para hablarme o escribir directamente.
                """.trimIndent()
            }
        }
    }
}
