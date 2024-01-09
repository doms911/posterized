
export const SidebarData = [
    {
        title: <img src="https://firebasestorage.googleapis.com/v0/b/posterized-8e1c4.appspot.com/o/preuzmi.png?alt=media&token=e2e76ce9-e25f-4649-bdd1-149d77ce9b0c" alt="Početna stranica" style={{ width: '40px', height: '40px' , margin: 'auto'}}/>,
        path: '/',
        cName: 'nav-homepage',
        allowedRoles: ['superadmin', 'admin', 'korisnik']
    },
    {
        title: 'Dodaj admina',
        path: '/addAdmin',
        cName: 'nav-text',
        allowedRoles: ['superadmin']
    },
    {
        title: 'Dodaj konferenciju',
        path: '/addConference',
        cName: 'nav-text',
        allowedRoles: ['superadmin']
    },
    {
        title: 'Prikaz konferencija',
        path: '/conferenceList',
        cName: 'nav-text',
        allowedRoles: ['superadmin']
    },
    {
        title: 'Dodaj sponzora',
        path: '/addSponsor',
        cName: 'nav-text',
        allowedRoles: ['admin']
    },
    {
        title: 'Dodaj rad i autora',
        path: '/addAuthor',
        cName: 'nav-text',
        allowedRoles: ['admin']
    },
    {
        title: 'Unos pina',
        path: '/pinInput',
        cName: 'nav-text',
        allowedRoles: ['superadmin', 'admin', 'korisnik']
    },
    {
        title: 'Glasaj',
        path: '/posters',
        cName: 'nav-text',
        allowedRoles: ['superadmin', 'admin', 'korisnik'],
        requiresPinValidation: true,
    },
    {
        title: 'Livestream',
        path: '/videoStream',
        cName: 'nav-text',
        allowedRoles: ['superadmin', 'admin', 'korisnik'],
        requiresPinValidation: true,
    },
    {
        title: 'Galerija',
        path: '/pictures',
        cName: 'nav-text',
        allowedRoles: ['superadmin', 'admin', 'korisnik'],
        requiresPinValidation: true,
    }
];