package cz.mendelu.pef.flashyflashcards.model

enum class BusinessCategory(val _name: String, val alias: String) {
    All(_name = "All", alias = "language_schools,libraries,culturalcenter,museums,galleries"),
    LanguageSchools(_name = "Language Schools", alias = "language_schools"),
    Libraries(_name = "Libraries", alias = "libraries"),
    CulturalCenter(_name = "Cultural Center", alias = "culturalcenter"),
    Museums(_name = "Museums", alias = "museums"),
    ArtGalleries(_name = "Art Galleries", alias = "galleries");

    companion object {
        fun getFromString(str: String): BusinessCategory {
            BusinessCategory.values().forEach {
                if (it._name == str) {
                    return it
                }
            }

            return All
        }
    }
}
