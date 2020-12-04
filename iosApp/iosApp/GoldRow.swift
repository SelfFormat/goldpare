import SwiftUI
import shared

struct GoldRow: View {
    
    var goldItem: GoldItem
    
    var body: some View {
        Button(action: {
            if let url = URL(string: goldItem.link) { UIApplication.shared.open(url) }
        }) {
            HStack() {
                VStack(alignment: .trailing){
                    Text(goldItem.website)
                }
                VStack(alignment: .leading, spacing: 8.0) {
                    let price = goldItem.price ?? "no price available"
                    Text(goldItem.title).bold()
                    Text(price)
                }
            }
            Spacer()
        }
    }
}
