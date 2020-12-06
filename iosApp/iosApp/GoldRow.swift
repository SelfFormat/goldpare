import SwiftUI
import shared

struct GoldRow: View {
    
    var goldItem: GoldItem
    
    var body: some View {
        Button(action: {
            if let url = URL(string: goldItem.link) { UIApplication.shared.open(url) }
        }) {
            let imageURL = goldItem.img_url ?? "nourl"
            HStack() {
                VStack(alignment: .trailing){
                    ImageView(withURL: imageURL)
                    Text(goldItem.website)
                }
                VStack(alignment: .leading, spacing: 8.0) {
                    let price = goldItem.price ?? "no price available"
                    let weight = goldItem.weight ?? "no weight available"
                    Text(goldItem.title).bold()
                    Text("quantity: \(goldItem.quantity)")
                    Text("weight: \(weight)")
                    Text(price)
                }
            }
            Spacer()
        }
    }
}

struct ImageView: View {
    @ObservedObject var imageLoader:ImageLoader
    @State var image:UIImage = UIImage()

    init(withURL url:String) {
        imageLoader = ImageLoader(urlString:url)
    }

    var body: some View {

            Image(uiImage: image)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width:100, height:100)
                .onReceive(imageLoader.didChange) { data in
                self.image = UIImage(data: data) ?? UIImage()
        }
    }
}
