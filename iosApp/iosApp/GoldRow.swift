import SwiftUI
import shared

struct GoldRow: View {
    var goldItem: GoldItem
    
    var body: some View {
        HStack() {
            VStack(alignment: .leading, spacing: 8.0) {
                let price = goldItem.price ?? "no price available"
                Text("Title: \(goldItem.title)")
                Text("Price: \(price)")
                Text("Link: \(goldItem.link)")
                Text("Website: \(goldItem.website)")
            }
            Spacer()
        }
    }
}
