package com.santelocale.data

import com.santelocale.data.entity.FoodItem

/**
 * Haitian Food Database - Pre-population data
 *
 * This matches the FOOD_DATABASE constant from mockup.md exactly.
 * Source: React prototype using Placehold.co for image URLs
 */
object FoodData {

    /**
     * Helper function to generate placeholder image URLs
     * Matches: const getImageUrl = (name, color) => `https://placehold.co/400x300/${color}/ffffff?text=${encodeURIComponent(name)}`
     */
    private fun getImageUrl(name: String, color: String): String {
        return "https://placehold.co/400x300/$color/ffffff?text=${name.replace(" ", "+")}"
    }

    /**
     * All food items categorized as:
     * - VERT (Green) - À Volonté (Emerald-600: 059669) - Eat freely
     * - JAUNE (Yellow) - Modérément (Yellow-600: ca8a04) - Moderate portions
     * - ROUGE (Red) - À Éviter (Red-600: dc2626) - Avoid
     */
    fun getAllFoodItems(): List<FoodItem> = listOf(
        // VERT (Green) - À Volonté (Emerald-600: 059669)
        FoodItem(
            id = "f1",
            name = "Feuilles de Jute (Lalo)",
            category = "VERT",
            imageUrl = getImageUrl("Lalo", "059669"),
            tip = "Excellent ! Riche en fibres, ne fait pas monter le sucre."
        ),
        FoodItem(
            id = "f2",
            name = "Gombo (Kalalou)",
            category = "VERT",
            imageUrl = getImageUrl("Gombo", "059669"),
            tip = "Très bon pour stabiliser le diabète."
        ),
        FoodItem(
            id = "f3",
            name = "Chayote (Mirliton)",
            category = "VERT",
            imageUrl = getImageUrl("Mirliton", "059669"),
            tip = "Mangez-en autant que vous voulez."
        ),
        FoodItem(
            id = "f4",
            name = "Avocat (Zaboka)",
            category = "VERT",
            imageUrl = getImageUrl("Avocat", "059669"),
            tip = "Bonnes graisses. Idéal pour couper la faim."
        ),
        FoodItem(
            id = "f5",
            name = "Poisson",
            category = "VERT",
            imageUrl = getImageUrl("Poisson", "059669"),
            tip = "Préférez le poisson en sauce ou grillé, pas frit."
        ),
        FoodItem(
            id = "f6",
            name = "Poulet (Dur)",
            category = "VERT",
            imageUrl = getImageUrl("Poulet", "059669"),
            tip = "Mangez la viande, évitez la peau grasse."
        ),
        FoodItem(
            id = "f7",
            name = "Œufs",
            category = "VERT",
            imageUrl = getImageUrl("Oeufs", "059669"),
            tip = "Excellente source de protéines."
        ),
        FoodItem(
            id = "f8",
            name = "Chou",
            category = "VERT",
            imageUrl = getImageUrl("Chou", "059669"),
            tip = "Parfait pour remplir l'estomac sans sucre."
        ),
        FoodItem(
            id = "f9",
            name = "Aubergine",
            category = "VERT",
            imageUrl = getImageUrl("Aubergine", "059669"),
            tip = "Délicieux en ragoût (Legim)."
        ),
        FoodItem(
            id = "f10",
            name = "Cresson",
            category = "VERT",
            imageUrl = getImageUrl("Cresson", "059669"),
            tip = "Plein de vitamines."
        ),

        // JAUNE (Yellow) - Modérément (Yellow-600: ca8a04)
        FoodItem(
            id = "f11",
            name = "Haricots (Pwa)",
            category = "JAUNE",
            imageUrl = getImageUrl("Haricots", "ca8a04"),
            tip = "Bon pour la santé, mais attention à la portion (une petite tasse)."
        ),
        FoodItem(
            id = "f12",
            name = "Banane Bouillie",
            category = "JAUNE",
            imageUrl = getImageUrl("Banane+Bouillie", "ca8a04"),
            tip = "Mangez 2 ou 3 morceaux maximum avec des légumes."
        ),
        FoodItem(
            id = "f13",
            name = "Fruit à Pain (Lam)",
            category = "JAUNE",
            imageUrl = getImageUrl("Lam", "ca8a04"),
            tip = "C'est un féculent. À manger avec modération."
        ),
        FoodItem(
            id = "f14",
            name = "Patate Douce",
            category = "JAUNE",
            imageUrl = getImageUrl("Patate+Douce", "ca8a04"),
            tip = "Meilleur que la pomme de terre, mais reste sucré."
        ),
        FoodItem(
            id = "f15",
            name = "Mais Moulu",
            category = "JAUNE",
            imageUrl = getImageUrl("Mais+Moulu", "ca8a04"),
            tip = "Mangez toujours avec beaucoup de légumes verts."
        ),
        FoodItem(
            id = "f16",
            name = "Igname",
            category = "JAUNE",
            imageUrl = getImageUrl("Igname", "ca8a04"),
            tip = "Un morceau suffit."
        ),

        // ROUGE (Red) - À Éviter (Red-600: dc2626)
        FoodItem(
            id = "f17",
            name = "Riz Blanc",
            category = "ROUGE",
            imageUrl = getImageUrl("Riz+Blanc", "dc2626"),
            tip = "Attention ! Le riz blanc fait monter le sucre très vite."
        ),
        FoodItem(
            id = "f18",
            name = "Banane Pesée",
            category = "ROUGE",
            imageUrl = getImageUrl("Banane+Peze", "dc2626"),
            tip = "La friture n'est pas bonne. Préférez la banane bouillie."
        ),
        FoodItem(
            id = "f19",
            name = "Pain Blanc",
            category = "ROUGE",
            imageUrl = getImageUrl("Pain+Blanc", "dc2626"),
            tip = "Farine blanche raffinée. À éviter."
        ),
        FoodItem(
            id = "f20",
            name = "Akasan",
            category = "ROUGE",
            imageUrl = getImageUrl("Akasan", "dc2626"),
            tip = "Trop de sucre et de lait condensé."
        )
    )

    /**
     * Category metadata matching React's categories object
     */
    data class FoodCategory(
        val label: String,
        val description: String,
        val color: String
    )

    val categories = mapOf(
        "VERT" to FoodCategory(
            label = "À Volonté",
            description = "Mangez tous les jours.",
            color = "059669"
        ),
        "JAUNE" to FoodCategory(
            label = "Modérément",
            description = "Petites quantités seulement.",
            color = "ca8a04"
        ),
        "ROUGE" to FoodCategory(
            label = "À Éviter",
            description = "Dangereux pour le sucre.",
            color = "dc2626"
        )
    )
}
