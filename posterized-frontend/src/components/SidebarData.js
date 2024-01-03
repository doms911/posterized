
export const SidebarData = [
    {
        title: 'Početna stranica',
        path: '/',
        cName: 'nav-text',
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
        title: 'Unos pina',
        path: '/pinInput',
        cName: 'nav-text',
        allowedRoles: ['superadmin', 'admin', 'korisnik']
    },
    {
        title: 'Dodaj sponzora',
        path: '/link',
        cName: 'nav-text',
        allowedRoles: ['admin']
    },
    {
        title: 'Dodaj rad i autora',
        path: '/link',
        cName: 'nav-text',
        allowedRoles: ['admin']
    },
];