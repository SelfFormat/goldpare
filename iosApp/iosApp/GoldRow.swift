import SwiftUI
import shared

struct GoldRow: View {
    var goldItem: GoldItem
    
    var body: some View {
        HStack() {
            VStack(alignment: .leading, spacing: 8.0) {
                Text("Title: \(goldItem.title)")
                Text("Price: \(goldItem.price)")
                Text("Link: \(goldItem.link)")
                Text("Website: \(goldItem.website)")
            }
            Spacer()
        }
    }
}
