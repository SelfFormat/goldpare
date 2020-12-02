import SwiftUI
import shared

struct ContentView: View {
  @ObservedObject private(set) var viewModel: ViewModel

    var body: some View {
        NavigationView {
            listView()
            .navigationBarTitle("Goldpare - compare gold prices")
            .navigationBarItems(trailing:
                Button("Reload") {
                    self.viewModel.loadGoldItems(forceReload: true)
            })
        }
    }

    private func listView() -> AnyView {
        switch viewModel.goldItems {
        case .loading:
            return AnyView(Text("Loading...").multilineTextAlignment(.center))
        case .result(let goldItems):
            return AnyView(List(goldItems) { goldItem in
                GoldRow(goldItem: goldItem)
            })
        case .error(let description):
            return AnyView(Text(description).multilineTextAlignment(.center))
        }
    }
}

extension ContentView {

    enum LoadableLaunches {
        case loading
        case result([GoldItem])
        case error(String)
    }

    class ViewModel: ObservableObject {
        let sdk: GoldSDK
        @Published var goldItems = LoadableLaunches.loading

        init(sdk: GoldSDK) {
            self.sdk = sdk
            self.loadGoldItems(forceReload: false)
        }

        func loadGoldItems(forceReload: Bool) {
            self.goldItems = .loading
            sdk.getGoldItems(forceReload: forceReload, completionHandler: { goldItems, error in
                if let goldItems = goldItems {
                    self.goldItems = .result(goldItems)
                } else {
                    self.goldItems = .error(error?.localizedDescription ?? "error")
                }
            })
        }
    }
}

extension GoldItem: Identifiable { }
