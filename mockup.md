import React, { useState, useEffect } from 'react';
import { 
  Droplets, 
  Utensils, 
  Footprints, 
  ChevronLeft, 
  Save, 
  History, 
  WifiOff, 
  Leaf, 
  AlertTriangle, 
  Trash2,
  Settings
} from 'lucide-react';

// --- DATA: HAITIAN FOOD DATABASE (With Images) ---
// Using Placehold.co to simulate local images with correct category coding
const getImageUrl = (name, color) => `https://placehold.co/400x300/${color}/ffffff?text=${encodeURIComponent(name)}`;

const FOOD_DATABASE = [
  // VERT (Green) - À Volonté (Emerald-600: 059669)
  { id: 'f1', name: 'Feuilles de Jute (Lalo)', category: 'VERT', image: getImageUrl('Lalo', '059669'), tip: 'Excellent ! Riche en fibres, ne fait pas monter le sucre.' },
  { id: 'f2', name: 'Gombo (Kalalou)', category: 'VERT', image: getImageUrl('Gombo', '059669'), tip: 'Très bon pour stabiliser le diabète.' },
  { id: 'f3', name: 'Chayote (Mirliton)', category: 'VERT', image: getImageUrl('Mirliton', '059669'), tip: 'Mangez-en autant que vous voulez.' },
  { id: 'f4', name: 'Avocat (Zaboka)', category: 'VERT', image: getImageUrl('Avocat', '059669'), tip: 'Bonnes graisses. Idéal pour couper la faim.' },
  { id: 'f5', name: 'Poisson', category: 'VERT', image: getImageUrl('Poisson', '059669'), tip: 'Préférez le poisson en sauce ou grillé, pas frit.' },
  { id: 'f6', name: 'Poulet (Dur)', category: 'VERT', image: getImageUrl('Poulet', '059669'), tip: 'Mangez la viande, évitez la peau grasse.' },
  { id: 'f7', name: 'Œufs', category: 'VERT', image: getImageUrl('Oeufs', '059669'), tip: 'Excellente source de protéines.' },
  { id: 'f8', name: 'Chou', category: 'VERT', image: getImageUrl('Chou', '059669'), tip: 'Parfait pour remplir l’estomac sans sucre.' },
  { id: 'f9', name: 'Aubergine', category: 'VERT', image: getImageUrl('Aubergine', '059669'), tip: 'Délicieux en ragoût (Legim).' },
  { id: 'f10', name: 'Cresson', category: 'VERT', image: getImageUrl('Cresson', '059669'), tip: 'Plein de vitamines.' },

  // JAUNE (Yellow) - Modérément (Yellow-600: ca8a04)
  { id: 'f11', name: 'Haricots (Pwa)', category: 'JAUNE', image: getImageUrl('Haricots', 'ca8a04'), tip: 'Bon pour la santé, mais attention à la portion (une petite tasse).' },
  { id: 'f12', name: 'Banane Bouillie', category: 'JAUNE', image: getImageUrl('Banane+Bouillie', 'ca8a04'), tip: 'Mangez 2 ou 3 morceaux maximum avec des légumes.' },
  { id: 'f13', name: 'Fruit à Pain (Lam)', category: 'JAUNE', image: getImageUrl('Lam', 'ca8a04'), tip: 'C\'est un féculent. À manger avec modération.' },
  { id: 'f14', name: 'Patate Douce', category: 'JAUNE', image: getImageUrl('Patate+Douce', 'ca8a04'), tip: 'Meilleur que la pomme de terre, mais reste sucré.' },
  { id: 'f15', name: 'Mais Moulu', category: 'JAUNE', image: getImageUrl('Mais+Moulu', 'ca8a04'), tip: 'Mangez toujours avec beaucoup de légumes verts.' },
  { id: 'f16', name: 'Igname', category: 'JAUNE', image: getImageUrl('Igname', 'ca8a04'), tip: 'Un morceau suffit.' },

  // ROUGE (Red) - À Éviter (Red-600: dc2626)
  { id: 'f17', name: 'Riz Blanc', category: 'ROUGE', image: getImageUrl('Riz+Blanc', 'dc2626'), tip: 'Attention ! Le riz blanc fait monter le sucre très vite.' },
  { id: 'f18', name: 'Banane Pesée', category: 'ROUGE', image: getImageUrl('Banane+Peze', 'dc2626'), tip: 'La friture n\'est pas bonne. Préférez la banane bouillie.' },
  { id: 'f19', name: 'Pain Blanc', category: 'ROUGE', image: getImageUrl('Pain+Blanc', 'dc2626'), tip: 'Farine blanche raffinée. À éviter.' },
  { id: 'f20', name: 'Akasan', category: 'ROUGE', image: getImageUrl('Akasan', 'dc2626'), tip: 'Trop de sucre et de lait condensé.' },
];

// --- COMPONENTS ---

const Header = ({ title, goBack, onSettings }) => (
  <div className="bg-emerald-700 text-white p-4 shadow-md flex items-center justify-between sticky top-0 z-10 shrink-0">
    <div className="flex items-center gap-3">
      {goBack && (
        <button onClick={goBack} className="p-1 hover:bg-emerald-600 rounded-full transition-colors">
          <ChevronLeft size={32} />
        </button>
      )}
      <h1 className="text-xl font-bold tracking-wide">{title}</h1>
    </div>
    <div className="flex items-center gap-3">
      <div className="flex items-center gap-1 text-xs bg-emerald-800 px-2 py-1 rounded-full opacity-90">
        <WifiOff size={14} />
        <span>Hors Ligne</span>
      </div>
      {onSettings && (
        <button onClick={onSettings} className="p-1 hover:bg-emerald-600 rounded-full transition-colors">
          <Settings size={24} />
        </button>
      )}
    </div>
  </div>
);

const BigButton = ({ icon: Icon, title, subtitle, colorClass, onClick }) => (
  <button 
    onClick={onClick}
    className={`${colorClass} w-full p-6 rounded-2xl shadow-lg text-white flex items-center gap-5 transition-transform transform active:scale-95 mb-4`}
  >
    <div className="bg-white/20 p-4 rounded-full">
      <Icon size={40} strokeWidth={2.5} />
    </div>
    <div className="text-left">
      <h2 className="text-2xl font-bold">{title}</h2>
      <p className="text-white/90 text-sm font-medium mt-1">{subtitle}</p>
    </div>
  </button>
);

// --- VIEWS ---

const Dashboard = ({ onNavigate, lastLog, userName, unit }) => {
  return (
    <div className="flex flex-col h-full bg-slate-50">
      <Header title={userName ? `Bonjour, ${userName}` : "Santé Locale"} onSettings={() => onNavigate('settings')} />
      
      <main className="flex-1 p-5 overflow-y-auto">
        {/* Status Card */}
        <div className="bg-white border-l-8 border-emerald-500 rounded-lg shadow-sm p-5 mb-8 flex items-start justify-between">
          <div>
            <h3 className="text-slate-500 font-bold text-sm uppercase tracking-wider mb-1">Dernière Glycémie</h3>
            {lastLog ? (
              <div>
                <span className="text-4xl font-extrabold text-slate-800">
                    {lastLog.displayValue || lastLog.value}
                </span>
                <span className="text-slate-500 ml-1 font-medium">{unit}</span>
                <p className="text-emerald-600 font-bold mt-1 text-sm">
                  {/* Basic check logic - simplistic for MVP */}
                  {unit === 'mmol/L' 
                    ? (lastLog.value < 7.8 ? '✅ Normal' : '⚠️ Attention') 
                    : (lastLog.value < 140 ? '✅ Normal' : '⚠️ Attention')
                  }
                </p>
              </div>
            ) : (
              <p className="text-slate-400 italic">Aucune donnée aujourd'hui</p>
            )}
          </div>
          <button onClick={() => onNavigate('history')} className="text-emerald-600 hover:bg-emerald-50 p-2 rounded-lg">
             <History size={24} />
          </button>
        </div>

        {/* The Big Three Actions */}
        <div className="space-y-4">
          <BigButton 
            icon={Droplets} 
            title="Ma Glycémie" 
            subtitle="Saisir mon taux de sucre" 
            colorClass="bg-blue-600 hover:bg-blue-700"
            onClick={() => onNavigate('glucose')}
          />
          
          <BigButton 
            icon={Utensils} 
            title="Guide Alimentaire" 
            subtitle="Bon vs Mauvais" 
            colorClass="bg-emerald-600 hover:bg-emerald-700"
            onClick={() => onNavigate('food')}
          />
          
          <BigButton 
            icon={Footprints} 
            title="Exercice" 
            subtitle="J'ai bougé aujourd'hui" 
            colorClass="bg-orange-500 hover:bg-orange-600"
            onClick={() => onNavigate('activity')}
          />
        </div>
      </main>
    </div>
  );
};

const SettingsView = ({ settings, onUpdateSettings, onClearData, onBack }) => {
  const [name, setName] = useState(settings.name || '');
  const [unit, setUnit] = useState(settings.unit || 'mg/dL');

  const handleSave = () => {
    onUpdateSettings({ name, unit });
    onBack();
  };

  const handleClear = () => {
      if(window.confirm("Êtes-vous sûr de vouloir tout effacer ?")) {
          onClearData();
          onBack();
      }
  }

  return (
    <div className="flex flex-col h-full bg-slate-50">
      <Header title="Paramètres" goBack={onBack} />
      <div className="p-6 space-y-8 overflow-y-auto flex-1">
        {/* Name Input */}
        <div className="space-y-2">
          <label className="text-slate-600 font-bold uppercase tracking-wider text-sm">Votre Nom</label>
          <input 
            type="text" 
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full p-4 rounded-xl border-2 border-slate-200 text-lg font-medium focus:border-emerald-500 outline-none"
            placeholder="Ex: Papa"
          />
        </div>

        {/* Unit Selection */}
        <div className="space-y-2">
          <label className="text-slate-600 font-bold uppercase tracking-wider text-sm">Unité de Glycémie</label>
          <div className="flex gap-3">
            <button 
              onClick={() => setUnit('mg/dL')}
              className={`flex-1 p-4 rounded-xl border-2 font-bold transition-all ${unit === 'mg/dL' ? 'border-emerald-500 bg-emerald-50 text-emerald-700 shadow-sm' : 'border-slate-200 bg-white text-slate-400'}`}
            >
              mg/dL
            </button>
            <button 
              onClick={() => setUnit('mmol/L')}
              className={`flex-1 p-4 rounded-xl border-2 font-bold transition-all ${unit === 'mmol/L' ? 'border-emerald-500 bg-emerald-50 text-emerald-700 shadow-sm' : 'border-slate-200 bg-white text-slate-400'}`}
            >
              mmol/L
            </button>
          </div>
          <p className="text-xs text-slate-400 mt-1">mg/dL est le standard en Haïti. mmol/L est utilisé en Europe/Canada.</p>
        </div>

        <button onClick={handleSave} className="w-full bg-emerald-600 text-white p-4 rounded-xl font-bold text-lg shadow-lg hover:bg-emerald-700 mt-8 active:scale-95 transition-transform">
          Enregistrer
        </button>
        
        <div className="pt-8 mt-4 border-t border-slate-200">
           <button onClick={handleClear} className="w-full bg-red-50 text-red-500 p-4 rounded-xl font-bold text-lg hover:bg-red-100 flex items-center justify-center gap-2 border border-red-100">
             <Trash2 size={20}/> Effacer les données
           </button>
        </div>
      </div>
    </div>
  )
}

const GlucoseInput = ({ onSave, onBack, unit }) => {
  const [value, setValue] = useState('');

  const handleNum = (num) => {
    // Limit length to prevent overflow
    if (value.length < 5) setValue(prev => prev + num);
  };

  const handleDot = () => {
    // Only allow one decimal point (comma)
    if (!value.includes(',')) {
      setValue(prev => prev ? prev + ',' : '0,');
    }
  };

  const handleDelete = () => {
    setValue(prev => prev.slice(0, -1));
  };

  const handleSave = () => {
    if (!value) return;
    // Replace comma with dot for parsing
    const cleanValue = value.replace(',', '.');
    onSave({ type: 'GLUCOSE', value: parseFloat(cleanValue), displayValue: value, date: new Date() });
    onBack();
  };

  return (
    <div className="flex flex-col h-full bg-slate-50">
      <Header title="Nouvelle Glycémie" goBack={onBack} />
      
      {/* Added overflow-y-auto to allow scrolling on small screens if needed */}
      <div className="flex-1 flex flex-col p-4 max-w-md mx-auto w-full overflow-y-auto">
        {/* Display Area */}
        <div className="bg-white rounded-xl shadow-inner border-2 border-slate-200 p-6 mb-4 text-center h-32 flex flex-col justify-center items-center shrink-0">
          <span className="text-slate-500 font-medium text-lg mb-1">Votre taux de sucre ?</span>
          <div className="flex items-end justify-center gap-2">
            <span className={`text-5xl font-extrabold ${value ? 'text-slate-900' : 'text-slate-300'}`}>
              {value || '--'}
            </span>
            <span className="text-slate-500 text-lg font-bold mb-2">{unit}</span>
          </div>
        </div>

        {/* Custom Keypad */}
        <div className="grid grid-cols-3 gap-3 flex-1 mb-4 min-h-[300px]">
          {[1, 2, 3, 4, 5, 6, 7, 8, 9].map(num => (
            <button 
              key={num}
              onClick={() => handleNum(num)}
              className="bg-white text-slate-800 text-3xl font-bold rounded-xl shadow-sm border-b-4 border-slate-200 active:border-b-0 active:translate-y-1 transition-all flex items-center justify-center py-2"
            >
              {num}
            </button>
          ))}
          {/* Decimal Button (Comma for French) */}
          <button 
            onClick={handleDot}
            className="bg-slate-50 text-slate-800 text-3xl font-bold rounded-xl shadow-sm border-b-4 border-slate-200 active:border-b-0 active:translate-y-1 transition-all flex items-center justify-center py-2"
          >
            ,
          </button>
          
          <button 
            onClick={() => handleNum(0)}
            className="bg-white text-slate-800 text-3xl font-bold rounded-xl shadow-sm border-b-4 border-slate-200 active:border-b-0 active:translate-y-1 transition-all flex items-center justify-center py-2"
          >
            0
          </button>
          
          <button 
            onClick={handleDelete}
            className="bg-slate-100 text-red-500 rounded-xl shadow-sm border-b-4 border-slate-200 active:border-b-0 active:translate-y-1 transition-all flex items-center justify-center py-2"
          >
            <Trash2 size={28} />
          </button>
        </div>

        {/* Save Button - made shrink-0 to prevent squishing */}
        <button 
          onClick={handleSave}
          disabled={!value}
          className={`w-full py-5 rounded-2xl text-white font-bold text-xl shadow-lg flex items-center justify-center gap-2 shrink-0 transition-all ${
            value ? 'bg-emerald-600 hover:bg-emerald-700' : 'bg-slate-300 cursor-not-allowed'
          }`}
        >
          <Save size={24} />
          ENREGISTRER
        </button>
      </div>
    </div>
  );
};

const FoodGuide = ({ onBack }) => {
  const [activeTab, setActiveTab] = useState('VERT');
  const [selectedFood, setSelectedFood] = useState(null);

  const categories = {
    VERT: { label: 'À Volonté', color: 'bg-emerald-600', text: 'text-emerald-700', border: 'border-emerald-200', desc: 'Mangez tous les jours.' },
    JAUNE: { label: 'Modérément', color: 'bg-yellow-500', text: 'text-yellow-700', border: 'border-yellow-200', desc: 'Petites quantités seulement.' },
    ROUGE: { label: 'À Éviter', color: 'bg-red-600', text: 'text-red-700', border: 'border-red-200', desc: 'Dangereux pour le sucre.' },
  };

  const filteredFoods = FOOD_DATABASE.filter(f => f.category === activeTab);

  return (
    <div className="flex flex-col h-full bg-slate-50">
      <Header title="Guide Alimentaire" goBack={onBack} />

      {/* Tabs */}
      <div className="flex shrink-0">
        {Object.entries(categories).map(([key, config]) => (
          <button
            key={key}
            onClick={() => { setActiveTab(key); setSelectedFood(null); }}
            className={`flex-1 py-4 font-bold text-sm tracking-wide transition-colors ${
              activeTab === key ? `${config.color} text-white` : 'bg-white text-slate-500 border-b-2 border-slate-100'
            }`}
          >
            {config.label.toUpperCase()}
          </button>
        ))}
      </div>

      {/* Description Banner */}
      <div className={`p-3 text-center text-sm font-medium ${categories[activeTab].text} bg-white border-b ${categories[activeTab].border} shrink-0`}>
        {activeTab === 'VERT' && <Leaf className="inline w-4 h-4 mr-1 mb-1" />}
        {activeTab === 'ROUGE' && <AlertTriangle className="inline w-4 h-4 mr-1 mb-1" />}
        {categories[activeTab].desc}
      </div>

      {/* List */}
      <div className="flex-1 overflow-y-auto p-4 space-y-4">
        {filteredFoods.map(food => (
          <div 
            key={food.id}
            onClick={() => setSelectedFood(selectedFood?.id === food.id ? null : food)}
            className={`bg-white rounded-xl shadow-sm border-2 overflow-hidden transition-all ${
              selectedFood?.id === food.id ? 'border-blue-500 ring-2 ring-blue-100' : 'border-transparent'
            }`}
          >
            {/* Image & Title Card */}
            <div className="relative">
               <img 
                 src={food.image} 
                 alt={food.name}
                 className="w-full h-32 object-cover"
               />
               <div className="absolute bottom-0 left-0 w-full bg-gradient-to-t from-black/80 to-transparent p-3 pt-8">
                 <h3 className="font-bold text-lg text-white drop-shadow-md">{food.name}</h3>
               </div>
            </div>
            
            {/* Expanded Details */}
            {selectedFood?.id === food.id && (
              <div className="bg-slate-50 p-4 border-t border-slate-100 text-slate-700 text-lg leading-relaxed animate-in slide-in-from-top-2">
                💡 {food.tip}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

const ActivityInput = ({ onSave, onBack }) => {
  const activities = [
    { label: 'Marche (30 min)', icon: '🚶🏾‍♂️', value: 30 },
    { label: 'Ménage', icon: '🧹', value: 20 },
    { label: 'Jardinage', icon: '🌿', value: 45 },
    { label: 'Danse / Sport', icon: '💃🏾', value: 30 },
  ];

  return (
    <div className="flex flex-col h-full bg-slate-50">
      <Header title="Activité" goBack={onBack} />
      
      <div className="flex-1 p-6 flex flex-col justify-center overflow-y-auto">
        <h2 className="text-xl font-bold text-slate-700 text-center mb-8">Qu'avez-vous fait aujourd'hui ?</h2>
        
        <div className="grid grid-cols-1 gap-4">
          {activities.map((act, idx) => (
            <button
              key={idx}
              onClick={() => {
                onSave({ type: 'ACTIVITY', value: act.value, label: act.label, date: new Date() });
                onBack();
              }}
              className="bg-white p-6 rounded-2xl shadow-md border-2 border-transparent hover:border-emerald-500 flex items-center gap-4 transition-all active:scale-95"
            >
              <span className="text-4xl">{act.icon}</span>
              <span className="text-xl font-bold text-slate-700">{act.label}</span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
};

const HistoryView = ({ logs, onBack, unit }) => {
  return (
    <div className="flex flex-col h-full bg-slate-50">
      <Header title="Historique" goBack={onBack} />
      
      <div className="flex-1 overflow-y-auto p-4">
        {logs.length === 0 ? (
          <div className="text-center text-slate-400 mt-20">
            <History size={48} className="mx-auto mb-2 opacity-50" />
            <p>Aucun historique disponible.</p>
          </div>
        ) : (
          <div className="space-y-3">
            {logs.map((log, index) => (
              <div key={index} className="bg-white p-4 rounded-xl shadow-sm flex items-center justify-between">
                <div className="flex items-center gap-3">
                  <div className={`p-2 rounded-full ${
                    log.type === 'GLUCOSE' ? 'bg-blue-100 text-blue-600' : 'bg-orange-100 text-orange-600'
                  }`}>
                    {log.type === 'GLUCOSE' ? <Droplets size={20} /> : <Footprints size={20} />}
                  </div>
                  <div>
                    <p className="font-bold text-slate-800">
                      {log.type === 'GLUCOSE' ? 'Glycémie' : log.label || 'Activité'}
                    </p>
                    <p className="text-xs text-slate-500">
                      {new Date(log.date).toLocaleDateString('fr-FR', { weekday: 'short', day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit' })}
                    </p>
                  </div>
                </div>
                <div className="text-right">
                  <span className="text-xl font-black text-slate-700">
                    {/* Prefer displayValue if available (keeps comma), otherwise fallback to number */}
                    {log.displayValue || log.value}
                  </span>
                  <span className="text-xs font-bold text-slate-400 ml-1">
                    {log.type === 'GLUCOSE' ? unit : 'min'}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

// --- MAIN APP CONTAINER ---

const App = () => {
  const [view, setView] = useState('dashboard'); // dashboard, glucose, food, activity, history, settings
  const [logs, setLogs] = useState([]);
  const [settings, setSettings] = useState({ name: '', unit: 'mg/dL' });

  // Load data from local storage on mount
  useEffect(() => {
    const savedLogs = localStorage.getItem('sante_locale_logs');
    if (savedLogs) setLogs(JSON.parse(savedLogs));

    const savedSettings = localStorage.getItem('sante_locale_settings');
    if (savedSettings) setSettings(JSON.parse(savedSettings));
  }, []);

  // Save data to local storage whenever changes
  useEffect(() => {
    localStorage.setItem('sante_locale_logs', JSON.stringify(logs));
  }, [logs]);

  useEffect(() => {
    localStorage.setItem('sante_locale_settings', JSON.stringify(settings));
  }, [settings]);

  const addLog = (log) => {
    setLogs([log, ...logs]);
  };

  const handleClearData = () => {
      setLogs([]);
      setSettings({ name: '', unit: 'mg/dL' });
  };

  const getLastGlucose = () => logs.find(l => l.type === 'GLUCOSE');

  // View Routing
  return (
    <div className="w-full h-screen bg-gray-900 flex justify-center items-center font-sans antialiased">
      {/* Mobile Frame Container */}
      <div className="w-full max-w-[420px] h-full sm:h-[90vh] bg-slate-50 sm:rounded-3xl shadow-2xl overflow-hidden relative flex flex-col">
        
        {view === 'dashboard' && (
          <Dashboard 
            onNavigate={setView} 
            lastLog={getLastGlucose()}
            userName={settings.name}
            unit={settings.unit}
          />
        )}
        
        {view === 'settings' && (
          <SettingsView
             settings={settings}
             onUpdateSettings={setSettings}
             onClearData={handleClearData}
             onBack={() => setView('dashboard')}
          />
        )}

        {view === 'glucose' && (
          <GlucoseInput 
            onSave={addLog} 
            onBack={() => setView('dashboard')}
            unit={settings.unit} 
          />
        )}
        
        {view === 'food' && (
          <FoodGuide 
            onBack={() => setView('dashboard')} 
          />
        )}

        {view === 'activity' && (
          <ActivityInput 
            onSave={addLog} 
            onBack={() => setView('dashboard')} 
          />
        )}

        {view === 'history' && (
          <HistoryView 
            logs={logs} 
            onBack={() => setView('dashboard')}
            unit={settings.unit} 
          />
        )}

      </div>
    </div>
  );
};

export default App;